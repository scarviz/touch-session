package db

import (
	"define"
	"fmt"
	"github.com/ziutek/mymysql/mysql"
	_ "github.com/ziutek/mymysql/thrsafe"
	"logger"
	"os"
	"sync"
	"time"
)

/*
エラーを検出する
*/
func checkError(err error) bool {
	if err != nil {
		fmt.Println(err)
		logger.LogPrintln(logger.ERR, err)
		return true
	}
	return false
}

/*
エラーを検出し、実行結果を返す
*/
func checkedResult(rows []mysql.Row, res mysql.Result, err error) ([]mysql.Row, mysql.Result, bool) {
	iserr := checkError(err)
	return rows, res, iserr
}

/*
DBコネクション
*/
var DBConn mysql.Conn

/*
ミューテックス
*/
var lockDB sync.Mutex

/*
DBをOpenする
*/
func Open(user, pass string) {
	proto := "tcp"
	addr := "127.0.0.1:3306"
	dbname := "SESSION_DB"

	// 接続情報を作成し、DBに接続する
	DBConn = mysql.New(proto, "", addr, user, pass, dbname)
	iserr := checkError(DBConn.Connect())

	if iserr {
		fmt.Println("接続に失敗しました")
		logger.LogPrintln(logger.ERR, "接続に失敗しました")
		os.Exit(define.DB_E_Open)
	} else {
		fmt.Printf("%sに接続しました\n", dbname)
		logger.LogPrintf(logger.DBG, "%sに接続しました\n", dbname)
	}
}

/*
DBをCloseする
*/
func Close() {
	iserr := checkError(DBConn.Close())

	if iserr {
		fmt.Println("接続を閉じるのに失敗しました")
		logger.LogPrintln(logger.ERR, "接続を閉じるのに失敗しました")
		os.Exit(define.DB_E_Close)
	} else {
		fmt.Println("接続を閉じました")
		logger.LogPrintln(logger.DBG, "接続を閉じました")
	}
}

/*
INSERT文を実行する(SOUND_DATA_TB)
*/
func InsertSoundTb(filepath string) (insId uint64) {
	// ロック開始
	lockDB.Lock()

	logger.LogPrintln(logger.DBG, "[InsertSoundTb] filepath:", filepath)

	// INSERT文作成
	sqlstr := "INSERT INTO SOUND_DATA_TB(SOUND_FILE_PATH, CREATE_TIME) VALUES ('%s', '%04d-%02d-%02d %02d:%02d:%02d')"

	now := time.Now()
	// Query実行(パラメータは可変)
	_, res, iserr := checkedResult(DBConn.Query(sqlstr, filepath, now.Year(), now.Month(), now.Day(), now.Hour(), now.Minute(), now.Second()))

	if iserr {
		fmt.Println("SOUND_DATA_TBへのinsertに失敗しました")
		logger.LogPrintln(logger.ERR, "SOUND_DATA_TBへのinsertに失敗しました")
		insId = uint64(0)
	} else {
		// Insertしたときの自動採番のPRIMARY KEY(id)を取得する
		insId = res.InsertId()
	}

	// ロック終了
	lockDB.Unlock()
	return insId
}

/*
INSERT文を実行する(COMPOSITION_DATA_TB)
*/
func InsertCompDataTb(compdata define.CompositionData) (compId uint64) {
	// ロック開始
	lockDB.Lock()

	logger.LogPrintln(logger.DBG, "[InsertCompDataTb] start")

	// トランザクション開始
	tr, err := DBConn.Begin()
	checkError(err)

	// INSERT文作成
	// 構成マスタ
	mssqlstr := "INSERT INTO COMPOSITION_MASTER(RHYTHM, TITLE) VALUES (%f, '%s')"
	// 構成データテーブル
	cmpsqlstr := "INSERT INTO COMPOSITION_DATA_TB(COMP_MS_ID, INDEX, SOUND_ID) VALUES (%d, %d, %d)"

	// Query実行
	_, res, iserr := checkedResult(tr.Query(mssqlstr, compdata.Rhythm, compdata.Title))

	if iserr {
		fmt.Println("COMPOSITION_MASTERへのinsertに失敗しました")
		logger.LogPrintln(logger.ERR, "COMPOSITION_MASTERへのinsertに失敗しました")

		// ロールバックする
		tr.Rollback()

		// ロック終了
		lockDB.Unlock()

		compId = uint64(0)
		return
	}

	// Insertしたときの自動採番のPRIMARY KEY(id)を取得する
	compId = res.InsertId()

	for _, sdata := range compdata.SoundData {
		// Query実行
		_, _, issounderr := checkedResult(tr.Query(cmpsqlstr, compId, sdata.Index, sdata.SoundID))

		if issounderr {
			fmt.Println("COMPOSITION_DATA_TBへのinsertに失敗しました")
			logger.LogPrintln(logger.ERR, "COMPOSITION_DATA_TBへのinsertに失敗しました")

			// ロールバックする
			tr.Rollback()

			// ロック終了
			lockDB.Unlock()

			compId = uint64(0)
			return
		}
	}

	tr.Commit()
	logger.LogPrintln(logger.DBG, "[InsertCompDataTb] Commit")

	// ロック終了
	lockDB.Unlock()

	return
}

/*
音データのファイルパスを取得する
*/
func GetSoundFileName(soundid int) (filename string) {
	// ロック開始
	lockDB.Lock()

	logger.LogPrintln(logger.DBG, "[GetSoundFileName] soundid：", soundid)
	// SELECT文作成
	sqlstr := "SELECT SOUND_FILE_PATH path FROM SOUND_DATA_TB WHERE SOUND_ID = %d"

	// Query実行(パラメータは可変)
	rows, res, iserr := checkedResult(DBConn.Query(sqlstr, soundid))

	size := 0
	if iserr {
		fmt.Println("[GetSoundFileName] ファイルパス取得に失敗しました")
		logger.LogPrintln(logger.ERR, "[GetSoundFileName] ファイルパス取得に失敗しました")
	} else {
		size = len(rows)
		logger.LogPrintln(logger.DBG, "[GetSoundFileName] 取得件数：", size)
	}

	// カラムのマッピング
	path := res.Map("path")

	filename = ""
	for _, row := range rows {
		filename = row.Str(path)
		if filename != "" {
			break
		}
	}

	// ロック終了
	lockDB.Unlock()

	return
}

/*
音データのIDをファイルパスから取得する
*/
func GetSoundIDByFileName(filename string) (soundid int) {
	// ロック開始
	lockDB.Lock()

	logger.LogPrintln(logger.DBG, "[GetSoundIDByFileName] filename：", filename)
	// SELECT文作成
	sqlstr := "SELECT SOUND_ID id FROM SOUND_DATA_TB WHERE SOUND_FILE_PATH = '%s'"

	// Query実行(パラメータは可変)
	rows, res, iserr := checkedResult(DBConn.Query(sqlstr, filename))

	size := 0
	if iserr {
		fmt.Println("[GetSoundIDByFileName] ファイルパス取得に失敗しました")
		logger.LogPrintln(logger.ERR, "[GetSoundIDByFileName] ファイルパス取得に失敗しました")
	} else {
		size = len(rows)
		logger.LogPrintln(logger.DBG, "[GetSoundIDByFileName] 取得件数：", size)
	}

	// カラムのマッピング
	id := res.Map("id")

	soundid = 0
	for _, row := range rows {
		soundid = row.Int(id)
	}

	// ロック終了
	lockDB.Unlock()

	return
}

/*
構成データリストを取得する
*/
func GetCompDataList() ([]define.CompDataList, []int) {
	// ロック開始
	lockDB.Lock()

	logger.LogPrintln(logger.DBG, "[GetCompDataList] start")

	// 音データIDリスト
	soundidlist := make([]int, 0)

	// SELECT文作成
	sqlstr := "SELECT ID id, RHYTHM rhythm, TITLE title FROM COMPOSITION_MASTER"

	// Query実行
	rows, res, iserr := checkedResult(DBConn.Query(sqlstr))

	size := 0
	if iserr {
		fmt.Println("[GetCompDataList] 構成データリスト取得に失敗しました")
		logger.LogPrintln(logger.ERR, "[GetCompDataList] 構成データリスト取得に失敗しました")

		compdummy := make([]define.CompDataList, 0)
		return compdummy, soundidlist
	} else {
		size = len(rows)
		logger.LogPrintln(logger.DBG, "[GetCompDataList] 取得件数：", size)
	}

	// カラムのマッピング
	id := res.Map("id")
	rhythm := res.Map("rhythm")
	title := res.Map("title")

	sounddata := make([]int, 16)
	complist := make([]define.CompDataList, 0, size)
	for _, row := range rows {
		complist = append(complist,
			define.CompDataList{
				CompID:      row.Int(id),
				Rhythm:      row.Float(rhythm),
				Title:       row.Str(title),
				Composition: sounddata})
	}

	sqlsoundstr := "SELECT cmptb.COMP_INDEX compindex, cmptb.SOUND_ID soundid FROM COMPOSITION_MASTER cmpms INNER JOIN COMPOSITION_DATA_TB cmptb ON cmpms.ID = cmptb.COMP_MS_ID WHERE cmpms.ID = %d"

	for cnt, compval := range complist {
		// Query実行
		rowssound, ressound, iserrsound := checkedResult(DBConn.Query(sqlsoundstr, compval.CompID))

		sizesound := 0
		if iserrsound {
			fmt.Println("[GetCompDataList] 構成データリストの音データ取得に失敗しました", compval.CompID)
			logger.LogPrintln(logger.ERR, "[GetCompDataList] 構成データリストの音データ取得に失敗しました", compval.CompID)

			continue
		} else {
			sizesound = len(rowssound)
			logger.LogPrintln(logger.DBG, "[GetCompDataList] 取得件数：", sizesound)
		}

		// カラムのマッピング
		index := ressound.Map("compindex")
		soundid := ressound.Map("soundid")

		i := 0
		sid := 0
		ok := false
		tmpmap := make(map[int]int)
		for _, rsound := range rowssound {
			i = rsound.Int(index)
			sid = rsound.Int(soundid)
			complist[cnt].Composition[i] = sid

			_, ok = tmpmap[sid]
			if !ok {
				soundidlist = append(soundidlist, sid)
				tmpmap[sid] = sid
			}
		}
	}

	// ロック終了
	lockDB.Unlock()

	return complist, soundidlist
}

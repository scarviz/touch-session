package page

import (
	"db"
	"define"
	"encoding/binary"
	"encoding/json"
	"fmt"
	"logger"
	"math"
	"net/http"
	"os"
	"path/filepath"
	"strconv"
	"sync"
	"wave"
)

/*
ミューテックス
*/
var lockPage sync.Mutex

/*
NFCデータから音データを取得するページ
*/
func NFCSound(w http.ResponseWriter, r *http.Request) {
	logger.LogPrintln(logger.DBG, "[NFCSound] start url:", r.URL)
	// 設定された文字列を取得
	nfcdata := r.FormValue("nfcdata")

	// sounddataディレクトリを作成する(既にある場合は無視する)
	errdir := os.MkdirAll("sounddata", 0755)
	if errdir != nil {
		logger.LogPrintln(logger.ERR, "[NFCSound] make dir err:", errdir)
		return
	}

	// ロック開始
	lockPage.Lock()

	// waveファイル名
	filename := filepath.Join("sounddata", "nfc"+nfcdata+"Wave.wav")

	// ファイルパスからファイル情報取得
	_, staterr := os.Stat(filename)

	// 情報が取得できた場合は既にファイルが存在する
	if staterr == nil {
		logger.LogPrintln(logger.DBG, "[NFCSound] already wave file:", filename)

		// 音データIDを取得する
		soundid := db.GetSoundIDByFileName(filename)

		// ロック終了
		lockPage.Unlock()

		// IDをJSON形式にする
		res := getSoundIDResJSONStr(int(soundid))
		w.Header().Add("Content-type", "application/json")
		// 検索結果を返す
		fmt.Fprintf(w, res)

		return
	}

	val, err := strconv.ParseFloat(nfcdata, 64)
	if err != nil {
		logger.LogPrintln(logger.ERR, "[NFCSound] parse err:", err)
		val = 1.0
	}

	// waveデータ調整用
	adjust := int(math.Ceil(441.0 * math.Pow(1.0+math.Sin(val), 2)))

	// waveファイルを作成する(Macの場合はBigEndian)
	//wave.CreateWaveFile(filename, adjust, binary.BigEndian)
	wave.CreateWaveFile(filename, adjust, binary.LittleEndian)

	// 音データをDBに登録する
	sid := db.InsertSoundTb(filename)

	// ロック終了
	lockPage.Unlock()

	// IDをJSON形式にする
	res := getSoundIDResJSONStr(int(sid))
	w.Header().Add("Content-type", "application/json")
	// 検索結果を返す
	fmt.Fprintf(w, res)
}

/*
音データをリクエストするページ
*/
func RequestSoundData(w http.ResponseWriter, r *http.Request) {
	logger.LogPrintln(logger.DBG, "[RequestSoundData] start url:", r.URL)
	// 設定された文字列を取得
	soundid := r.FormValue("soundid")

	id, err := strconv.ParseInt(soundid, 10, 32)

	filename := ""
	if err != nil {
		logger.LogPrintln(logger.ERR, "[RequestSoundData] parse err:", err)
	} else {
		// 音データのファイルパスを取得する
		filename = db.GetSoundFileName(int(id))
	}

	w.Header().Add("Content-type", "application/force-download")
	http.ServeFile(w, r, filename)
}

/*
音データIDをJSON文字列にして取得する
*/
func getSoundIDResJSONStr(soundid int) string {
	resp := define.SoundIDRes{soundid}
	jsonstr, _ := json.Marshal(resp)
	return string(jsonstr)
}

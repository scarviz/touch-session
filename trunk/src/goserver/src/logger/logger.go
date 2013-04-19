package logger

import (
	"log"
	"os"
	"strconv"
	"sync"
)

type LogLv int

const (
	ERR LogLv = 1 << iota // エラー用
	WRN                   // 警告用
	INF                   // ユーザ向けの情報用
	DBG                   // デバッグ用
)

// デフォルトのログファイル名
const DEF_LOG_FILE_NAME string = "/debug.log"

// 最大ファイルサイズ(5MB)
const MAX_FILE_SIZE int64 = 5 * 1024 * 1024

// ログファイルの管理世代数
const MAX_LOG_GENERATION = 3

// ログファイル
var logFile *os.File

// ログ出力モード(ログレベルで出力を制御する)
var logMode LogLv

/*
ミューテックス
*/
var lockLog sync.Mutex

/*
* ロガー初期化処理
 */
func InitLogger(filePath string, outputLogLv LogLv) (err error) {
	// 出力先が入力されていない場合はカレントに出力する
	if filePath == "" {
		cur, _ := os.Getwd()
		filePath = cur + DEF_LOG_FILE_NAME
	}
	// ログ出力モードを設定
	logMode = outputLogLv

	// ログファイルが存在しなければ追記モードで新規作成
	logFile, err = os.OpenFile(filePath, os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)

	if err != nil {
		return
	}

	// ログファイルにlogで書き込めるように設定
	log.SetOutput(logFile)
	LogPrintln(DBG, "log start")

	return
}

/*
* クローズ処理
 */
func CloseLog() error {
	// ログ出力レベルのチェック
	if (logMode & DBG) != 0 {
		// 出力モードにビットが立ってる場合は出力
		// 世代管理でもクローズすることがあるのでlockはしない
		name := "[" + getLogLvName(DBG) + "]"
		log.Println(name, "log close")
	}

	// ファイルクローズ
	err := logFile.Close()
	return err
}

/*
* ログを出力する(Printf)
 */
func LogPrintf(loglv LogLv, format string, a ...interface{}) {
	// ログ出力レベルのチェック
	if (logMode & loglv) == 0 {
		// 出力モードにビットが立っていない場合は出力しない
		return
	}

	// ロック開始
	lockLog.Lock()

	checkFileSize()

	format = "%s " + format
	name := "[" + getLogLvName(loglv) + "]"
	param := append([]interface{}{
		name,
	}, a...)
	log.Printf(format, param...)

	// ロック終了
	lockLog.Unlock()
}

/*
* ログを出力する(Println)
 */
func LogPrintln(loglv LogLv, a ...interface{}) {
	// ログ出力レベルのチェック
	if (logMode & loglv) == 0 {
		// 出力モードにビットが立っていない場合は出力しない
		return
	}

	// ロック開始
	lockLog.Lock()

	checkFileSize()

	name := "[" + getLogLvName(loglv) + "]"
	param := append([]interface{}{
		name,
	}, a...)
	log.Println(param...)

	// ロック終了
	lockLog.Unlock()
}

/*
* ログを出力する(Print)
 */
func LogPrint(loglv LogLv, a ...interface{}) {
	// ログ出力レベルのチェック
	if (logMode & loglv) == 0 {
		// 出力モードにビットが立っていない場合は出力しない
		return
	}

	// ロック開始
	lockLog.Lock()

	checkFileSize()

	name := "[" + getLogLvName(loglv) + "]"
	param := append([]interface{}{
		name,
	}, a...)
	log.Print(param...)

	// ロック終了
	lockLog.Unlock()
}

/*
* ログ出力レベルの名前を取得する
 */
func getLogLvName(loglv LogLv) (name string) {
	switch loglv {
	case ERR:
		name = "ERR"
	case WRN:
		name = "WRN"
	case INF:
		name = "INF"
	case DBG:
		name = "DBG"
	}
	return
}

/*
* ファイルサイズを確認する
 */
func checkFileSize() error {
	fileinfo, err := logFile.Stat()

	// ファイル情報が取得できない場合は処理を中止する
	if err != nil {
		return err
	}

	// ファイルサイズを確認し、最大ファイルサイズより大きい場合は、
	// リネームを行う
	if fileinfo.Size() > MAX_FILE_SIZE {
		err = renameLogFile()
	}

	return err
}

/*
* ログファイルのリネームを行う
 */
func renameLogFile() (err error) {
	// ファイル名(パス)取得
	fileName := logFile.Name()
	// 一度クローズする
	CloseLog()

	for i := MAX_LOG_GENERATION; i > 1; i-- {
		genFileName := fileName + "_back" + strconv.FormatInt(int64(i), 10) + ".log"
		backGenFileName := fileName + "_back" + strconv.FormatInt(int64(i-1), 10) + ".log"

		// リネーム先ファイルが存在する場合は削除
		os.Remove(genFileName)
		// リネームする(カウントアップ)
		os.Rename(backGenFileName, genFileName)
	}

	// リネーム処理
	os.Rename(fileName, fileName+"_back1.log")

	// ログの再作成
	err = InitLogger(fileName, logMode)

	return
}

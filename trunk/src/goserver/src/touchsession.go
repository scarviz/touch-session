package main

import (
	"db"
	"define"
	"flag"
	"fmt"
	"logger"
	"net/http"
	"os"
	"page"
	"strconv"
)

func main() {
	// コマンドパラメータ定義
	flag1 := flag.String("path", "/usr/local/TouchSession", "実行ファイルのディレクトリパス")
	flag2 := flag.String("port", "59702", "ポート番号")
	flag3 := flag.Bool("d", true, "Daemonを実行するかどうか")
	flag4 := flag.String("user", "tsdbuser", "DBユーザ名")
	flag5 := flag.String("pass", "", "DBパスワード")
	flag6 := flag.Int("loglv", 15, "ログ出力レベル(ビット) ERR:1,WRN:2,INF:4,DBG:8")
	// コマンドパラメータの解析
	flag.Parse()

	exeDirPath := *flag1
	address := ":" + *flag2
	isDaemon := *flag3
	user := *flag4
	pass := *flag5
	loglv := logger.LogLv(*flag6) // LogLv型に変換

	if isDaemon {
		// daemon実行
		errcd := Daemon(0, 0)

		if errcd != 0 {
			fmt.Println("daemon err!!")
			os.Exit(define.E_Daemon)
		}
	}

	// カレントディレクトリを実行ファイルのパスにしておく
	os.Chdir(exeDirPath)

	// ロガーの初期化
	logger.InitLogger("", loglv)

	// TSDBを開く
	db.Open(user, pass)

	// 遅延実行でDBとログファイルを閉じる
	defer func() {
		db.Close()
		logger.CloseLog()
	}()

	// nfcsoundページ
	http.HandleFunc("/touchsession/nfcsound", page.NFCSound)
	// reqsounddataページ
	http.HandleFunc("/touchsession/reqsounddata", page.RequestSoundData)
	// regcompdataページ
	http.HandleFunc("/touchsession/regcompdata", page.RegistCompositionData)
	// reqcomplistページ
	http.HandleFunc("/touchsession/reqcomplist", page.RequestCompDataList)
	http.Handle("/sounddata/", http.StripPrefix("/sounddata/", http.FileServer(http.Dir("sounddata"))))

	// Webサーバを開始する
	err := http.ListenAndServe(address, nil)

	// エラーが発生した場合にここに到達する
	if err != nil {
		logger.LogPrintln(logger.ERR, "[ErrCD]"+strconv.FormatInt(int64(define.E_Listen), 10)+" ", err)
		os.Exit(define.E_Listen)
	}

}

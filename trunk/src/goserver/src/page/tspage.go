package page

import (
	"encoding/binary"
	"logger"
	"math"
	"net/http"
	"os"
	"path/filepath"
	"strconv"
	"wave"
)

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

	w.Header().Add("Content-type", "application/force-download")
	// waveファイル名
	filename := filepath.Join("sounddata", "nfc"+nfcdata+"Wave.wav")

	// ファイルパスからファイル情報取得
	_, staterr := os.Stat(filename)

	// 情報が取得できた場合は既にファイルが存在する
	if staterr == nil {
		logger.LogPrintln(logger.DBG, "[NFCSound] already wave file:", filename)

		http.ServeFile(w, r, filename)
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

	http.ServeFile(w, r, filename)
}

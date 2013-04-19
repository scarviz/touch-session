package wave

import (
	"bytes"
	"encoding/binary"
	"fmt"
	"logger"
	"os"
)

/*
PCMWaveフォーマット
*/
type PCMWAVEFORMAT struct {
	wf             WAVEFORMAT
	wBitsPerSample uint16 // サンプルあたりのビット数 (bit/sample)
}

/*
Waveフォーマット
*/
type WAVEFORMAT struct {
	wFormatTag      uint16 // フォーマットID
	nChannels       uint16 // チャンネル数
	nSamplesPerSec  uint32 // サンプリングレート
	nAvgBytesPerSec uint32 // データ速度 (Byte/sec)
	nBlockAlign     uint16 // ブロックサイズ(Byte/sample×チャンネル数)
}

/*
バイナリデータ書き込み用
*/
type writeInfo struct {
	file  *os.File
	order binary.ByteOrder
	data  *[]byte
}

/*
Waveファイルを作成する
*/
func CreateWaveFile(filename string, adjust int, order binary.ByteOrder) {
	logger.LogPrintln(logger.DBG, "[CreateWaveFile] start filename, adjust:", filename, adjust)

	pfm_size := uint32(16) // PCMWAVEFORMATのサイズ(byte)
	playtime := 1          // 再生時間(s)

	// ヘッダー情報を作成する(Waveフォーマット)
	pfm := PCMWAVEFORMAT{
		wBitsPerSample: uint16(16),
		wf: WAVEFORMAT{
			wFormatTag:     uint16(1),
			nChannels:      uint16(2),
			nSamplesPerSec: uint32(22050),
		}}
	pfm.wf.nAvgBytesPerSec = (uint32(pfm.wf.nChannels) * pfm.wf.nSamplesPerSec * uint32(pfm.wBitsPerSample)) / uint32(8)
	pfm.wf.nBlockAlign = (pfm.wf.nChannels * pfm.wBitsPerSample) / uint16(8)

	// 再生時間分のデータサイズ
	data_size := pfm.wf.nAvgBytesPerSec * uint32(playtime)

	// waveデータ (余計なデータを格納しないように、
	// 長さは0にして、キャパシティをデータサイズ分用意する)
	wave_data := make([]int16, 0, data_size)

	// データ書き込みループ
	loop := (int(pfm.wf.nSamplesPerSec) / adjust) / 2

	for sec := 0; sec < playtime; sec++ {
		for j := 0; j < adjust; j++ {
			for i := 0; i < loop; i++ {
				wave_data = append(wave_data, int16(800)) // 音量
			}
			for i2 := 0; i2 < loop; i2++ {
				wave_data = append(wave_data, int16(-800)) // -音量
			}
		}
	}

	// ファイルサイズ(データサイズ + 36byte(ヘッダーサイズ44byte - 8)
	file_size := data_size + uint32(36)

	logger.LogPrintln(logger.DBG, "[CreateWaveFile] file_size:", file_size)

	// waveファイルをオープン
	file, err := os.Create(filename)
	if err != nil {
		fmt.Println("file create err:", err)
		logger.LogPrintln(logger.ERR, "[CreateWaveFile] file create err:", err)
		return
	}
	// バイナリデータを書き込むための情報を用意する
	b := make([]byte, 0)
	info := writeInfo{file: file, order: order, data: &b}

	// ヘッダーのバイナリデータを作成する
	writeBinaryData([]uint8("RIFF"), info)
	writeBinaryData(file_size, info)
	writeBinaryData([]uint8("WAVE"), info)
	writeBinaryData([]uint8("fmt "), info)
	writeBinaryData(pfm_size, info)
	writeBinaryData(pfm.wf.wFormatTag, info)
	writeBinaryData(pfm.wf.nChannels, info)
	writeBinaryData(pfm.wf.nSamplesPerSec, info)
	writeBinaryData(pfm.wf.nAvgBytesPerSec, info)
	writeBinaryData(pfm.wf.nBlockAlign, info)
	writeBinaryData(pfm.wBitsPerSample, info)
	writeBinaryData([]uint8("data"), info)
	writeBinaryData(data_size, info)

	// waveデータのバイナリデータを作成する
	for _, data := range wave_data {
		writeBinaryData(data, info)
	}

	// waveファイルへ書き込み
	errc := writeFile(info)
	if errc != nil {
		fmt.Println("file write err:", errc)
		logger.LogPrintln(logger.ERR, "[CreateWaveFile] file write err:", errc)
		return
	}

	logger.LogPrintln(logger.DBG, "[CreateWaveFile] file write ok.", filename)
}

/*
バイナリデータを書き込む
*/
func writeBinaryData(data interface{}, info writeInfo) error {
	// バイナリデータを作成する
	buf := new(bytes.Buffer)
	err := binary.Write(buf, info.order, data)

	if err != nil {
		return err
	}

	// バイナリデータを追加する
	*info.data = append(*info.data, buf.Bytes()...)

	return nil
}

/*
ファイルに書き込む
*/
func writeFile(info writeInfo) error {
	// バイナリデータをwaveファイルに書き込む
	_, err := info.file.Write(*info.data)
	if err != nil {
		return err
	}

	// ファイルを閉じる
	errc := info.file.Close()
	if errc != nil {
		return errc
	}

	return nil
}

package main

import (
	"os"
	"syscall"
)

/*
daemon関数
*/
func Daemon(nochdir, noclose int) int {
	var ret uintptr
	var err syscall.Errno

	// バックグラウンドプロセスにする
	// 子プロセスを生成し，親プロセスを終了する
	ret, _, err = syscall.Syscall(syscall.SYS_FORK, 0, 0, 0)
	if err != 0 {
		return -1
	}
	switch ret {
	case 0:
		break
	default:
		os.Exit(0)
	}

	// 新しいセッションを生成(セッションリーダになる)
	pid, _ := syscall.Setsid()
	if pid == -1 {
		return -1
	}

	if nochdir == 0 {
		// カレントディレクトリの再設定
		os.Chdir("/")
	}

	// ファイルのパーミッションを再設定
	syscall.Umask(0)

	if noclose == 0 {
		// 標準入出力先を/dev/nullファイルに変更して、すべて破棄する
		f, e := os.OpenFile("/dev/null", os.O_RDWR, 0)
		if e == nil {
			fd := int(f.Fd())
			syscall.Dup2(fd, int(os.Stdin.Fd()))
			syscall.Dup2(fd, int(os.Stdout.Fd()))
			syscall.Dup2(fd, int(os.Stderr.Fd()))
		}
	}

	return 0
}

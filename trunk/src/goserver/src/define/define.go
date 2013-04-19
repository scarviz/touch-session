package define

/*
構成データ
*/
type CompositionData struct {
	Rhythm    float32
	Title     string
	SoundData []CompSoundData
}

/*
音構成データ
*/
type CompSoundData struct {
	Index   int
	SoundID int
}
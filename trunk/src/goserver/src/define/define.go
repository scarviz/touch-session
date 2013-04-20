package define

/*
構成データ
*/
type CompositionData struct {
	Rhythm    float64
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

/*
音データID返却用
*/
type SoundIDRes struct {
	SoundID int `json:"soundid"`
}

/*
リクエストされる構成データ
*/
type ReqRegCompData struct {
	Title       string
	Rhythm      float64
	Composition []int
}

/*
構成データID返却用
*/
type CompIDRes struct {
	CompID int `json:"compid"`
}

/*
構成データリスト
*/
type CompDataList struct {
	CompID      int     `json:"compid"`
	Title       string  `json:"title"`
	Rhythm      float64 `json:"rhythm"`
	Composition []int   `json:"composition"`
}

/*
構成データリスト、音データIDリストのレスポンス用
*/
type RespCompList struct {
	CompDataList *[]CompDataList `json:"compdatalist"`
	SoundIDList  []int           `json:"soundidlist"`
}

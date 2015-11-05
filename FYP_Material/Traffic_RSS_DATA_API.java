行車時間顯示器
XML Item 							Spec
LOCATION_ID							行車時間顯示器的位置
DESTINATION_ID						行車時間顯示器的終點
CAPTURE_DATE						計算行車時間資料的時間(Format: “YYYY-MM-DD” T “HH:MM:SS”)
JOURNEY_TYPE						顯示內容的種類(1 – 行車時間資料 2 – 圖像資料)
JOURNEY_DATA						行車時間資料 
									(當“JOURNEY_TYPE = 1” – 分鐘以 2 位數顯示
									當“JOURNEY_TYPE = 2” – “1” 指交通擠塞,交通擠塞的圖像將會顯示 
									當“JOURNEY_TYPE = 2” – “2” 指隧道交通擠塞,隧道交通擠塞的圖像將會顯示 
									當“JOURNEY_TYPE = 2” – “3” 指隧道封閉,隧道封閉的圖像將會顯示 
									當“JOURNEY_TYPE = 2” – “4” 指無顯示,無顯示圖像將會顯示
									-1 – 不合適)
COLOUR_ID							行車時間資料的顏色(1 – 紅色 2 – 黃色 3 – 綠色 -1 – 不合適)
JOURNEY_DESC						行車時間資料的說明
									只出現於 “JOURNEY_TYPE” 是 “2”. 無內容指不合適
									當“JOURNEY_TYPE = 2” 及 “JOURNEY_DATA = 1” – 交通擠塞,交通擠塞的圖像將會顯示 
									當“JOURNEY_TYPE = 2” 及 “JOURNEY_DATA = 2” 隧道交通擠塞,隧道交通擠塞的圖像將會顯示 
									當“JOURNEY_TYPE = 2” 及 “JOURNEY_DATA = 3” – 隧道封閉,隧道封閉的圖像將會顯示 
									當“JOURNEY_TYPE = 2” 及 “JOURNEY_DATA = 4” – 無顯示,無顯示圖像將會顯示


行車速度圖
RSS / XML Item 						Spec.
LINK_ID								每條行車速度路線的名稱
REGION								行車速度資料的區域
ROAD_TYPE							行車速度資料的道路種類
ROAD_SATURATION_LEVEL				說明道路的交通狀況
TRAFFIC_SPEED						估計的平均行車速度
CAPTURE_DATE						計算行車速度的時間	

ImageList.XML
Request Method: GET
URL = "http://tdcctv.data.one.gov.hk/image_name" 
where image_name =  key.JPG

Tag id 		TAG Item Name
0			<image-list></image-list>
1 			<image></image>
2 			<key>BC109F</key>
3 			<english-region></english-region>
4 			<chinese-region>/<chinese-region>
5 			<english-description></english-description>
6			<chinese-description></chinese-description>
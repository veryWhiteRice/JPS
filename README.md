# JPS

# AndroidManifest.xml -> 안드로이드내 세부적인 기능을 작동할 때 백그라운드적으로 필요한 라이브러리 같은거 담는 곳(ex, 화면 전환 / 네트워크 통신 등)
# MainActivity.java -> 첫 layout 기능
# activity_main.xml -> 첫 layout
# FirstActivity.java -> 두 번째 layout 기능
# user.xml -> 두 번째 layout 페이지
# colors.xml -> 색상 담는 곳(drawable 같은거)
# -----------------------------
# 구인구직글 공고 목록
# BasicJob(자바코드), basic_job.xml, item_row.xml로 구성
 2 개의 xml로 구성한 이유는 csv 파일 크기가 크다보니 많은 양의 데이터를 UI로 표현하기 위해 RecyclerView를 이용하여 표현
 또한, 자바코드에서 비동기식 csv 접근 방식을 이용했음. 마찬가지로 데이터 크기가 커서 반응성이 너무 늦어 Async 이용
 (8.12 수정)
 - BasicJob 상세정보, 버튼 클릭 추가

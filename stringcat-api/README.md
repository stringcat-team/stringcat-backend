# stringcat-api
API 서버

- stringcat-api
    - stringcat-domain
        - stringcat-common

### lombok
- lombok 의존성을 가지고 있으므로, lombok plugin을 설치하기
- intellij > "Settings > Build > Compiler > Annotation Processors" enable Annotation Processing 체크하기

### REST API
- 각 도메인의 ReqDto/ResDto 안의 inner class로 관리
- get domain/{id}/search 👉🏻 단건 조회 (return : CustomInfo dto)
- get domain/fetch + pages 변수 👉🏻 검색 (return : Page)


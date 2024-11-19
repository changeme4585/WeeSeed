# WeeSeed 어플리케이션 사용법

<apk 관련>


배포용: 

https://github.com/PointOfViewS/WeeSeed_App/blob/WeeSeed_Test2/app/release

테스트용: 

https://github.com/PointOfViewS/WeeSeed_App/tree/WeeSeed_Test2/app/build/outputs/apk/debug

타겟 안드로이드 버전: 14

최소 안드로이드 버전: 12 (여기서도 테스트했음)

(설정>휴대전화 정보>소프트웨어 정보)


---




### <음성ai가 안됨>

그건 민강님 서버에서 돌아가므로 서버 켜달라고 요청하면됨





   
### <이스터에그>

1. 서버 ip주소 바꾸기: 로그인 화면 아기 얼굴 > 수정 후 SET 버튼 누르기 (위: 메인서버, 밑: 음성 ai 서버)

2. 카드 삭제: aac/비디오 카드의 우측 상단에 숨겨뒀음





---

### <빌드가 안됨>

local.properties의

sdk.dir=C\:\\Users\\Jiwon\\AppData\\Local\\Android\\Sdk 

이 부분을 자기 PC에 맞게 고쳐볼 것 (그랬는데도 안되면 어쩔 수 없음)


https://github.com/PointOfViewS/WeeSeed_App/blob/WeeSeed_Test2/local.properties

# WeeSeed
WeeSeed는 발달 장애 아동을 위한 **AI 기반 학습 및 교육 보조 프로그램**입니다.  
이 프로젝트는 장애아동의 학습을 돕고, 보호자 및 재활사의 교육 피로도를 감소시키며,  
아동의 **언어적·사회적 기능 향상**을 목표로 개발되었습니다.  
WeeSeed는 다양한 학습 방식과 직관적인 인터페이스로 장애아동의 학습 접근성을 높입니다.

---

## 주요 특징 🌟
- **AI 기반 학습**: 음성 및 시각 AI 기술을 활용하여 아동의 학습 효율성 증대.
- **AAC(보조 및 대체 의사소통) 기능**: 아동의 발화 연습과 학습 동기 부여.
- **비디오 자기 모델링 기법 지원**: 아동 스스로 자신의 행동을 학습하도록 돕는 기능.
- **학습 기록 관리**: 보호자 및 재활사를 위한 성장일지와 통계 데이터 제공.
- **직관적인 UI**: 아동의 인지 수준을 고려한 쉽고 간단한 사용자 인터페이스.

---

## 시스템 아키텍처 🛠️
WeeSeed는 다음과 같은 기술 스택을 활용하여 개발되었습니다.

| 기술 구성 | 세부 사항                     |
|-----------|--------------------------------|
| **프론트엔드** | React (웹), Android Native (모바일) |
| **백엔드** | Spring Boot, mysql, jpa                  |
| **클라우드 인프라** | AWS EC2, S3, RDS, Route 53 |
| **음성 AI** | Whisper, gTTS               |
| **시각 AI** | TensorFlow, MobileNetV2     |
| **배포 사항** | Nginx, Certbot              |


### 사용 기술 및 도구
- **IDE**: IntelliJ (백엔드), Visual Studio Code (웹), Android Studio (앱)
- **협업 도구**: Git, Figma, Notion
- **API 테스트**: Postman

---

## 주요 기능 🔧

### 1. AI 학습 기능
- **음성 인식**: 아동의 발음을 분석하고 점수를 매겨 학습 피드백 제공
- **이미지 적합성 검사**: 학습 자료로 사용되는 이미지의 적합성을 평가
- **카드 유사도 검사**: 기존 학습 데이터와 유사한 이미지를 자동 추천

### 2. AAC 카드 기능
- **이미지 카드**: 아동이 그림과 단어를 학습할 수 있도록 지원
- **비디오 카드**: 비디오 자기 모델링을 활용한 행동 학습 제공
- **카드 생성/수정/삭제**: 사용자 맞춤형 학습 자료 생성 가능

### 3. 성장일지 및 통계
- **성장일지**: 아동의 학습 내용을 날짜별로 기록 및 확인
- **통계 분석**: 아동별 학습량 및 데이터 시각화를 통해 학습 효과 추적

### 4. 사용자 맞춤 설정
- **보호자 및 재활사 기능**: 아동 관리 및 학습 데이터를 손쉽게 공유
- **유해 앱 알림**: 아동이 학습 외 활동에 몰두하지 않도록 차단

---

## 실행 방법 🚀
### - ⚠️ 현재 서버가 닫혀 있습니다. 서비스 실행이 불가능합니다.

### **WeeSeed Web**
- **URL**: ~~[WeeSeed Web 실행하기](https://myweeseed.com)~~

### **WeeSeed App**
- APK 파일은 아래 GitHub 레포지토리에서 다운로드할 수 있습니다.  
   - **배포용 APK 다운로드**: [Release APK](https://github.com/PointOfViewS/WeeSeed_App/blob/WeeSeed_Test2/app/release)  
   - **테스트용 APK 다운로드**: [Debug APK](https://github.com/PointOfViewS/WeeSeed_App/tree/WeeSeed_Test2/app/build/outputs/apk/debug)
- 시스템 요구 사항 📱
  - **타겟 안드로이드 버전**: 14  
  - **최소 안드로이드 버전**: 12 (해당 버전에서 테스트 완료)  
- 설치 방법
  1. APK 파일 다운로드 후 실행.
  2. 설치 전 **설정 > 휴대전화 정보 > 소프트웨어 정보**에서 안드로이드 버전을 확인하세요.
  3. 필요 시 **설정 > 보안 > 알 수 없는 소스 허용** 옵션을 활성화합니다.
     
---

## 프로젝트 팀 👥
- **GitHub 팀원 계정** : [<img src="https://avatars.githubusercontent.com/u/90965441?s=96&v=4" width="20" height="20">](https://github.com/heily-tech) [<img src="https://avatars.githubusercontent.com/u/129397073?s=96&v=4" width="20" height="20">](https://github.com/kmk9970) [<img src="https://avatars.githubusercontent.com/u/150992230?s=96&v=4" width="20" height="20">](https://github.com/SEjiji) [<img src="https://avatars.githubusercontent.com/u/93308824?s=96&v=4" width="20" height="20">](https://github.com/changeme4585) [<img src="https://avatars.githubusercontent.com/u/128035504?s=96&v=4" width="20" height="20">](https://github.com/tndu1212)

- **GitHub 레포지토리** : [PointOfViewS](https://github.com/orgs/PointOfViewS/repositories)

----

**문의 및 피드백**  
이메일: `edelbte@yu.ac.kr`  
웹사이트: [https://myweeseed.com](https://myweeseed.com)  

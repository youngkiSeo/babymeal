<p align="center">
  <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/416253d6-efc0-4c12-b417-6f049dc35b4b">
</p>


## 목차
- [프로젝트 소개](#프로젝트-소개)
- [주요 기능](#주요-기능)
- [웹페이지](#웹페이지)
- [결과 페이지](#결과-페이지)

# 이유식 판매 쇼핑몰 프로젝트

## 🖥️ 프로젝트 소개
JAVA / MariaDB / Spring Boot / JPA / MyBatis <br>
이유식 판매 쇼핑몰 프로젝트 <br>
참고 사이트 : https://www.alvins.co.kr/

## 🕰️ 개발 기간
* 23.07.14일 - 23.09.13일 
* 1차 : 23.07.14일 - 23.08.13
* 2차 : 23.08.14일 - 23.09.13 <br>
* 1차 기능 구현은 MyBatis이용
* 2차 프로젝트 기간동안 1차의 내용을 MyBatis -> JPA로 변경하여 구현 및 기능 추가

### 🧑‍🤝‍🧑 맴버구성 (백엔드)
- 팀장  : 서영기
- 팀원1 : 김다율
- 팀원2 : 이진규
- 팀원3 : 홍기윤

#### ⚙️ 개발 환경
- `Java`
- `JDK 1.8.0`
- **Framework** : Springboot
- **Database** : MariaDB
- **ORM** : Mybatis

## 📌 주요 기능
#### 로그인 및 회원가입 
- DB값 검증
- ID찾기, PW찾기 >> <small><i>스크린샷 참고</i></small>
- 로그인/로그아웃에 따른 토큰 생성 및 관리
- ID 중복 체크

#### 마이 페이지
- 회원정보 수정 기능
- 주문내역 조회
- 아기 관리 데이터 추가 기능(알러지, 기호 등)

#### 메인 페이지
- 로그인한 회원의 정보에 따른 상품 자동 추천 기능
- 베스트셀러 조회 기능

#### 검색
- 알러지/이유식 단계를 이용한 필터링 기능
- 형태소 분석기 및 한영변환을 이용하여 검색기능 구현
- redis 활용한 인기/최근검색어 조회

#### 상품
- 구매자를 위한 상품 데이터 조회
- 현 날짜와 시간을 확인하여 예상 배송일정 계산 
- 리뷰게시판 조회 및 작성

#### 주문
- DB조회 및 업데이트 
- 카카오페이를 이용한 결제 기능 구현

#### 관리자 페이지
- 웹에디터를 이용한 상품등록/수정/삭제 기능
- 주문내역 및 상품 조회/필터링/검색 기능
- 판매량 분석 기능 <br>
etc. 회원탈퇴, 배송상태 변경 등의 관리자용 기능





## 📌 웹페이지
* 구매자용 : http://112.222.157.156:5001/ <br>
* 관리자용 : http://112.222.157.156:5001/admin <br>
외부 접속 가능한 링크입니다. 구매자/관리자 페이지 모두 사용 가능합니다. <br>
> 이용가능 ID / PW : green502teama@gmail.com / WcANrCBW0Z <br>
> 관리자용 페이지도 이용 가능한 계정입니다.



## 📌 결과 페이지

|                                                                                                      |                                                                                                          | ??????                                                                                                   |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/416253d6-efc0-4c12-b417-6f049dc35b4b"> | <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/2606b755-84dc-442a-bbe4-31d5c5ca1dee"> | <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/153f1d8d-ded6-4543-b311-fd41b27f64bf"> |
| Intro                                                                                                    | 메인 페이지                                                                                                   | 상품 전체보기                                                                                                  |
| <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/98951ae4-86d1-483f-904a-4c20bc6cfc91"> | <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/048e1236-d271-43bd-b30a-a50460b97b63"> | <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/97e82501-5cbd-4f39-b876-c09694d2bc07"> |
| 로그인 페이지                                                                                                  | 마이페이지                                                                                                    | 검색기능                                                                                                     |
| <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/d2bd275b-47cb-47ed-b230-54ebbf839fc3"> | <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/df98fe8c-1989-4d63-9698-702d48fe7e76"> | <img src="https://github.com/youngkiSeo/babymeal/assets/132994346/231f60c7-0088-418b-9ae7-73ac871d262d"> |
| 검색 예시                                                                                             | 비밀번호찾기                                                                                                   | 비밀번호찾기                                                                                                   |

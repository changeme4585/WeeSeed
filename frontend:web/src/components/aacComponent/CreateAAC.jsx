/*AAC 카드 생성 틀* 이걸로 픽스!!*/
import React, { useState, useEffect } from "react";
import styled, { keyframes } from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import cardUploadIcon from "../../images/svg/add-icon-white.svg";
import SelectColor from "../SelectColor";
import axios from "axios";
import CardAnimation from "../AnimationComponent/CardAnimation"; // 애니메이션 주기
import AiTestModal from "./AiTestModal"; //ai 테스트 결과창!!
import micImg from "../../images/basicImg/mic-icon.png"; //음성녹음 아이콘
import ttsImg from "../../images/basicImg/tts-bot.png";
import afterRecordImg from "../../images/svg/white-check.svg"; //녹음 완료시 아이콘

//창 닫힐때 애니메이션
const fadeOut = keyframes`
  from {
    opacity: 1;
    transform: translateY(0px);
  }
  to {
    opacity: 0;
    transform: translateY(20px);
  }
`;

const CreateAACModalBg = styled.div`
  display: flex;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.6);
`;

const CreateAACModalBox = styled.div`
  display: flex;
  flex-direction: row;
  width: 65.625vw;
  height: 92.356vh;
  background-color: rgba(247, 246, 245, 1);
  border-radius: 10px;
  overflow: hidden;
  position: relative;
  &.fade-out {
    animation: ${fadeOut} 0.4s ease-out forwards;
  }
`;

const CreateAACLeftSection = styled.div`
  width: 69.05%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  background-color: rgba(182, 174, 165, 1);
`;

const CreateAACRightSection = styled.div`
  width: 30.95%;
  height: 100%;
  display: flex;
  justify-content: center;
  position: relative;
`;

const CreateAACImageUploadButton = styled.div`
  width: 17.24%;
  height: 17.24%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }
`;

const CreateAACImageInput = styled.input`
  display: none;
`;
//음성추가하기 label
const CreateAACLabel = styled.label`
  font-family: "Pretendard-Regular";
  top: 28.97%;
  left: 10.26%;
  position: absolute;
`;

const CreateAACCardNameInput = styled.input`
  font-family: "Pretendard-Regular";
  border: 1px solid #ffce1b !important;
  width: 77.95%;
  height: 5.52%;
  top: 9.2%;
  &.card-name-input {
    font-size: 1rem;
    box-shadow: none;
    color: var(--black);
  }
  &.card-name-input:hover {
    box-shadow: none;
    background-color: rgba(255, 239, 133, 1);
    outline: none;
  }
  &.card-name-input:focus {
    box-shadow: none;
    outline: none;
    background-color: rgba(255, 253, 234, 1);
  }
  position: absolute;
`;

const CreateAACTitle = styled.h2`
  font-family: "Pretendard-Regular";
  top: 2.53%;
  font-size: 2rem;
  position: absolute;
`;

const CreateAACButton = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 1rem;
  border: none;
  width: 79.49%;
  height: 4.6%;
  position: absolute;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
  }
  font-family: "Pretendard-Bold";
  bottom: 4.6%;
`;
const CreateAACAudioButton = styled.button`
  background-color: rgba(247, 246, 245, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 1rem;
  border: none;
  width: 25%;
  height: 12%;
  position: absolute;
  font-family: "Pretendard-Regular";
  top: 31.5%;
  left: 21%;

  img {
    background-color: rgba(255, 206, 27, 1);
    width: 60px;
    height: 60px;
    border-radius: 30px;
    box-shadow: 1px 1px 7px 0px rgba(0, 0, 0, 0.1);
    transition: background-color 0.3s ease;

    &:hover {
      background-color: rgba(255, 173, 0, 1);
      color: rgba(17, 17, 17, 1);
    }
  }
`;

  const CreateAACTTSButton

= styled . button ` background-color: rgba(247, 246, 245, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 1rem;
  border: none;
  width: 25%;
  height: 12%;
  position: absolute;
  font-family: "Pretendard-Regular";
  top: 31.5%;
  right: 21%;

  img {
    background-color: rgba(255, 206, 27, 1);
    width: 60px;
    height: 60px;
    border-radius: 30px;
    transition: background-color 0.3s ease;
    box-shadow: 1px 1px 7px 0px rgba(0, 0, 0, 0.1);

    &:hover {
      background-color: rgba(255, 173, 0, 1);
      color: rgba(17, 17, 17, 1);
    }
  }
`;

const CreateAACCloseButton = styled.button`
  background-color: transparent;
  border: none;
  cursor: pointer;
  position: absolute;
  top: 2.3%;
  right: 4.36%;
`;

const CreateAACCheckboxLabel = styled.label`
  display: flex;
  align-items: center;
  font-family: "Pretendard-Regular";
  bottom: 11.49%;
  right: 10.2%;
  cursor: pointer;
  position: absolute;
  p {
    font-family: "Pretendard-Regular";
    right: 3.08px;
    font-size: 1rem;
  }
`;

const CreateAACCheckbox = styled.input`
  opacity: 0;
  width: 0;
  height: 0;
  & + span {
    //체크 해제 상태
    border-color: rgba(255, 206, 27, 1);
    border-radius: 0; //체크박스 기본적으로 둥글한거 네모지게 만들기 ㅎ
  }
  &:checked + span {
    background-color: rgba(255, 206, 27, 1);
    border-color: rgba(255, 206, 27, 1);
  }
  &:checked + span:after {
    display: block;
  }
`;

const CreateAACCheckboxCustom = styled.span`
  width: 20px;
  height: 20px;
  background-color: transparent;
  border: 1px solid black;
  display: inline-block;
  position: relative;
  margin-left: 2px;
  &:after {
    content: "";
    position: absolute;
    display: none;
    left: 6px;
    top: 1px;
    width: 6px;
    height: 10px;
    border: solid white;
    border-width: 0 2px 2px 0;
    transform: rotate(45deg);
  }
`;

const CreateAACSelectColorContainer = styled.div`
  top: 17.93%;
  width: 79.48%;
  height: 5.52%;
  position: absolute;
`;

//ai 응답 전까지 로딩 문구 띄우기
const LoadingMessage = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: #f7f6f5;
  padding: 15px;
  border-radius: 8px;
  font-family: "Pretendard-Bold";
  z-index: 1000;
  display: flex;
  align-items: center;
`;

const dotsAnimation = keyframes`
  0% { content: ''; }
  25% { content: '.'; }
  50% { content: '..'; }
  75% { content: '...'; }
  100% { content: ''; }
`;

const AnimatedDots = styled.span`
  display: inline-block;
  width: 11px;
  margin-left: 2px;
  &::after {
    content: "";
    animation: ${dotsAnimation} 2.5s infinite steps(1);
  }
`;

const CreateAAC = ({ closeModal, isModalOpen, fetchAACCards }) => {
  const [image, setImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [cardName, setCardName] = useState("");
  const [audio, setAudio] = useState(null);
  const [isRecording, setIsRecording] = useState(false); //음성 녹음!!
  const [mediaRecorder, setMediaRecorder] = useState(null);
  const [color, setColor] = useState("#767676"); //이부분만 수정
  const [childCode, setChildCode] = useState("");
  const [constructorId, setConstructorId] = useState("");
  const [share, setShare] = useState(1);
  const [isClosing, setIsClosing] = useState(false); // 닫힐때 애니메이션
  const [isRecordingComplete, setIsRecordingComplete] = useState(false); //녹화 완료 확인
  const [isAiTestPass, setIsAiTestPass] = useState(false); //ai 테스트 통과 여부
  const [isCreate, setIsCreate] = useState(false); //카드 중복 생성 방지
  const [aiTestModalOpen, setAiTestModalOpen] = useState(false); //ai test 결과 팝업창!!
  const [aiTestResult, setAiTestResult] = useState("fail"); //ai test 결과 상태
  const [isLoading, setIsLoading] = useState(false); // ai 테스트 전 로딩 상태
  const detectedObjects = ['악세사리', '옷', '신발', '집', '씻기', '운동'];

  //tts 요청
  const requestTTS = async () => {
    if (!cardName) {
      alert("카드명을 입력해주세요!");
      return;
    }
    try {
      const TTSResponse = await axios.post(
          `${process.env.REACT_APP_SERVER_IP}/api/tts`,
          null,
          {
            params: { cardName },
            responseType: "blob",
          }
      );
      //audio blob 생성하고 저장..
      const audioBlob = new Blob([TTSResponse.data], { type: "audio/mpeg" });
      setAudio(audioBlob);
      //여기서부터 미리 재생 해보는 코드
      //테스트 성공하면 지워도 되고 안지워도 되고 고민을 해봄 ㅇㅇ
      // const audioUrl = URL.createObjectURL(audioBlob);
      // const audioElement = new Audio(audioUrl);
      // audioElement.play();
      const audioUrl = URL.createObjectURL(audioBlob);
      const audioElement = new Audio(audioUrl);
      audioElement.play().then(() => {
        URL.revokeObjectURL(audioUrl); // URL 해제
      });

    } catch (error) {
      console.error("TTS 요청 실패", error);
    }
  };

  //마이크 접근
  const requestMicroAccess = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      const recorder = new MediaRecorder(stream);
      setMediaRecorder(recorder);
    } catch (err) {
      console.error("마이크 접근 안됨:", err);
    }
  };

  useEffect(() => {
    requestMicroAccess();
  }, []);

  const startRecord = () => {
    setIsRecordingComplete(false);
    if (mediaRecorder) {
      mediaRecorder.start();
      setIsRecording(true);

      const audioChunks = [];
      mediaRecorder.ondataavailable = (event) => {
        audioChunks.push(event.data);
      };

      mediaRecorder.onstop = () => {
        const audioBlob = new Blob(audioChunks, { type: "audio/mp3" }); //mp3 형식
        setAudio(audioBlob);
        const audioUrl = URL.createObjectURL(audioBlob);
        const audioElement = new Audio(audioUrl);
        audioElement.load();
        setIsRecordingComplete(true); // 녹음 완료
      };
    }
  };

  const stopRecord = () => {
    if (mediaRecorder && isRecording) {
      mediaRecorder.stop();
      setIsRecording(false);
    }
  };
  useEffect(() => {
    const selectedChildCode = localStorage.getItem("selectedChildCode");
    const constructorIdFromStorage = localStorage.getItem("userId");
    if (selectedChildCode) {
      setChildCode(selectedChildCode);
    }
    if (constructorIdFromStorage) {
      setConstructorId(constructorIdFromStorage);
    }
  }, []);

  const handleImageUpload = async (e) => {
    const file = e.target.files[0];
    if (file) {
      if (imagePreview) {
        URL.revokeObjectURL(imagePreview);
      }
      setImage(file);
      setImagePreview(URL.createObjectURL(file));

      /****여기서부터 이미지 업로드 AI 코드****/
      setIsAiTestPass(false); // 이미지가 새로 선택될 때는 AI 테스트 초기화

      //로딩 시작
      setIsLoading(true);

      try {
        //이미지 업로드시 AI 이용해 적절한 이미지인지 식별
        const formData = new FormData();
        formData.append("file", file); //안되면 그냥 file로 바꾸기
        console.log("formData 내용:", Array.from(formData.entries()));

        const uploadResponse = await axios.post(
            `${process.env.REACT_APP_SERVER_IP}/image/upload`,
            formData
        );

        const responseData = uploadResponse.data; //서버에서 온 데이터 저장
        // 밝기 검사의 경우 반환 값이 아예 Error로 처리되어 생략

        // AI 테스트 통과 시
        if (responseData.usable) {
          console.log("이미지 업로드 AI 테스트 통과:", uploadResponse.data);
          setIsAiTestPass(true); // AI 테스트 통과

          // detected_object 처리
          // detectedObjects = ['악세사리', '옷', '신발', '집', '씻기', '운동']
          if (detectedObjects.includes(responseData.detected_object)) {
            console.log(`${responseData.detected_object} 감지됨:`, responseData);
            setAiTestResult(responseData.detected_object);
          } else {
            console.log("객체 감지:", responseData);
            setAiTestResult("success");
          }
        } else {
          console.log("예상치 못한 응답:", responseData);
          setImage(null); // 이미지 없애기
          setImagePreview(null); // 미리보기 초기화
          setIsAiTestPass(false); // AI 테스트 실패
          setAiTestResult("fail");
        }
        setAiTestModalOpen(true);
      } catch (error) {
          console.error("이미지 업로드 AI 테스트 서버 에러 발생:", error);
          if (error.response) {
            alert(`서버 에러: ${error.response.data}`);
          } else if (error.request) {
            alert("서버에서 받은 응답 없음");
          } else {
            alert("사진 업로드 중 알 수 없는 에러 발생");
          }
        setImage(null); // 이미지 없애기
        setImagePreview(null); // 미리보기 초기화
        setIsAiTestPass(false); // ai 테스트 실패
      } finally {
        //로딩 끝
        setIsLoading(false);
      }
    }
  };

  const handleCreateCard = async () => {
    if (!image || !cardName || !audio || !isAiTestPass) {
      //ai 테스트 통과해야 이미지 생성 가능 ㅇ.ㅇ
      alert("모든 정보를 입력해 주세요!");
      return;
    }
    //카드 중복 생성 방지
    if (isCreate) {
      return;
    }
    setIsCreate(true);
    const uniqueId = Date.now();
    const audioFilename = `recording-${uniqueId}.mp3`; //음성파일명
    //오디오, 이미지 확인
    console.log("Image:", image);

    //서버에 보낼땐 # 제거하기....
    const colorWithoutHash = color.replace("#", "");
    console.log("서버 보내기전 색깔 테스트:", colorWithoutHash);

    try {
      const formData = new FormData();
      formData.append("image", image);
      formData.append("cardName", cardName);
      formData.append("audio", audio, audioFilename);
      formData.append("color", colorWithoutHash);
      formData.append("childCode", childCode);
      formData.append("constructorId", constructorId);
      formData.append("share", share);

      await axios.post(
          `${process.env.REACT_APP_SERVER_IP}/aac/upload`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
      );
      const audioUrl = URL.createObjectURL(audio);
      const audioElement = new Audio(audioUrl);

      audioElement.onloadedmetadata = () => {
        console.log("Duration:", audioElement.duration); // Log the duration
      };
      fetchAACCards();
      closeModal();
    } catch (error) {
      console.error("카드 생성 실패:", error);
    } finally {
      setIsCreate(false); //카드 생성 상태 초기화. 버튼 여러번 클릭하면 카드 여러장 생성되는거 방지
    }
  };

  useEffect(() => {
    return () => {
      if (imagePreview) {
        URL.revokeObjectURL(imagePreview);
      }
    };
  }, [imagePreview]);

  //이미지 업로드 후 또 이미지 업로드 할수 있게 처리
  const handleImageClick = () => {
    document.getElementById("imageUpload").click();
  };
  const handleCheckboxChange = (e) => {
    setShare(e.target.checked ? 1 : 0);
  };

  const handleCloseButtonClick = () => {
    setIsClosing(true);
    setTimeout(() => {
      closeModal();
      setIsClosing(false);
    }, 200);
  };
  return (
      <>
        <CreateAACModalBg>
          <CardAnimation isOpen={isModalOpen}>
            <CreateAACModalBox className={isClosing ? "fade-out" : ""}>
              <CreateAACLeftSection>
                {isLoading && (
                    <LoadingMessage>
                      이미지 AI 검사 중
                      <AnimatedDots />
                    </LoadingMessage>
                )}
                {imagePreview ? (
                    <img
                        src={imagePreview}
                        alt="Uploaded"
                        style={{
                          width: "100%",
                          height: "100%",
                          objectFit: "cover",
                          cursor: "pointer",
                        }} //cover or contain or 없애기
                        onClick={handleImageClick}
                    />
                ) : (
                    <CreateAACImageUploadButton
                        onClick={() => document.getElementById("imageUpload").click()}
                    >
                      <img src={cardUploadIcon} alt="Upload" />
                    </CreateAACImageUploadButton>
                )}
                <CreateAACImageInput
                    id="imageUpload"
                    type="file"
                    accept="image/jpg, image/jpeg, image/png"
                    onChange={handleImageUpload}
                />
              </CreateAACLeftSection>
              <CreateAACRightSection>
                <CreateAACCloseButton onClick={handleCloseButtonClick}>
                  <img src={xButton} alt="Close" />
                </CreateAACCloseButton>
                <CreateAACTitle>말해봐요 추가</CreateAACTitle>
                <CreateAACCardNameInput
                    type="text"
                    placeholder="   카드명 입력"
                    className="card-name-input"
                    value={cardName}
                    onChange={(e) => setCardName(e.target.value)}
                />
                <CreateAACSelectColorContainer>
                  <SelectColor setColor={setColor} small />
                </CreateAACSelectColorContainer>

                <CreateAACLabel>음성 유형 선택 (녹음, TTS)</CreateAACLabel>
                <CreateAACAudioButton
                    onClick={isRecording ? stopRecord : startRecord}
                >
                  {isRecording || !isRecordingComplete ? (
                      <img src={micImg} alt="mic img" />
                  ) : (
                      <img src={afterRecordImg} alt="녹음 후" />
                  )}
                </CreateAACAudioButton>
                <CreateAACTTSButton onClick={requestTTS}>
                  <img src={ttsImg} alt="tts-img" />
                </CreateAACTTSButton>
                <CreateAACImageInput
                    id="audioUpload"
                    type="file"
                    accept="audio/*"
                    onChange={(e) => setAudio(e.target.files[0])}
                />
                <CreateAACCheckboxLabel>
                  <p>말해봐요 공유하기</p>
                  <CreateAACCheckbox
                      type="checkbox"
                      checked={share === 1}
                      onChange={handleCheckboxChange}
                  />
                  <CreateAACCheckboxCustom />
                </CreateAACCheckboxLabel>
                <CreateAACButton
                    className="create-btn"
                    onClick={handleCreateCard}
                >
                  확인
                </CreateAACButton>
              </CreateAACRightSection>
            </CreateAACModalBox>
          </CardAnimation>
        </CreateAACModalBg>
        {/*Ai 테스트 결과창*/}
        {aiTestModalOpen && (
            <AiTestModal
                isModalOpen={aiTestModalOpen}
                closeModal={() => setAiTestModalOpen(false)}
                testResult={aiTestResult}
                setCardName={(name) => setCardName(name)}
            />
        )}
      </>
  );
};
export default CreateAAC;
import React from "react";
import styled from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import axios from "axios";

const DeleteModalBg = styled.div`
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

const DeleteModalBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-evenly;
  width: 480px;
  height: 242px;
  background-color: #f7f6f5;
  border-radius: 15px;
  position: relative;
  box-shadow: 1px 3px 5px rgba(0, 0, 0, 0.2);

  h1 {
    font-size: 32px;
    font-family: "Pretendard-Bold";
    margin-top: 40px;
  }
  p {
    font-family: "Pretendard-Regular";
    margin-top: 25px;
    margin-bottom: 40px;
    font-size: 16px;
  }
  div {
    display: flex;
    justify-content: space-between;
    width: 300px;
    height: 36px;
    margin-bottom: 40px;
  }
`;

const DeleteModalButton = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 16px;
  border: none;
  width: 144px;
  height: 36px;
  font-family: "Pretendard-Regular";
  transition: background-color 0.3s ease;
  &:hover {
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
  }
`;

const DeleteModalButtonC = styled.button`
  background-color: #d9d4cf;
  color: #111111;
  cursor: pointer;
  font-size: 16px;
  border: none;
  width: 144px;
  height: 36px;
  font-family: "Pretendard-Regular";
  transition: background-color 0.3s ease;
  &:hover {
    background-color: #b6aea5;
    color: #111111;
  }
`;

const DeleteModalCloseButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const DeleteModal = ({ closeModal, card, fetchAACCards, fetchVideoCards }) => {
  // 카드 삭제 처리 함수
  const handleDelete = async () => {
    try {
      const constructorId = localStorage.getItem("userId");
      const cardId = card.id;

      const formData = new FormData();

      //카드 타입별로 서버에 다르게 보내기~!
      if (card.type === "aac") {
        formData.append("aacCardId", cardId);
      } else if (card.type === "video") {
        formData.append("videoCardId", cardId);
      } else if (card.type === "default") {
        formData.append("constructorId", constructorId); //디폴트 카드의 경우 유저 id 와 card name 보내기
        formData.append("cardName", card.imgName);
      } else {
        throw new Error("카드 타입 알수 없음;;");
      }

      // 카드 타입에 따라 다른 API 엔드포인트를 설정
      let endpoint;
      let successMessage;

      if (card.type === "default") {
        endpoint = `${process.env.REACT_APP_SERVER_IP}/default-card/delete`;
        successMessage = "기본 카드 삭제 성공!";
      } else if (card.type === "aac") {
        endpoint = `${process.env.REACT_APP_SERVER_IP}/aac/delete`;
        successMessage = "AAC 카드 삭제 성공!";
      } else if (card.type === "video") {
        endpoint = `${process.env.REACT_APP_SERVER_IP}/video/delete`;
        successMessage = "비디오 삭제 성공!";
      } else {
        throw new Error("카드 타입 알 수 없음"); // 알 수 없는 카드 타입 처리
      }

      // API 요청
      const response = await axios.post(endpoint, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      if (response.status === 200 || response.status === 204) {
        alert(successMessage); // 성공 메시지 알림
        card.type === "video" ? fetchVideoCards() : fetchAACCards(); // 카드 타입에 따라 적절한 데이터 가져오기
        closeModal(); // 모달 닫기
      } else {
        throw new Error("Unexpected response status");
      }
    } catch (error) {
      console.error("삭제 중 오류 발생:", error);
      alert("카드 삭제 실패!"); // 실패 메시지 알림
    }
  };

  return (
      <DeleteModalBg>
        <DeleteModalBox>
          <DeleteModalCloseButton onClick={closeModal}>
            <img src={xButton} alt="Close" />
          </DeleteModalCloseButton>
          <h1>정말 삭제하시겠습니까?</h1>
          <p>확인을 누르면 카드가 삭제됩니다.</p>
          <div>
            <DeleteModalButtonC onClick={closeModal}>취소</DeleteModalButtonC>
            <DeleteModalButton onClick={handleDelete}>확인</DeleteModalButton>
          </div>
        </DeleteModalBox>
      </DeleteModalBg>
  );
};

export default DeleteModal;

// const DeleteModal = ({
//                        closeModal,
//                        isModalOpen,
//                        card,
//                        fetchAACCards,
//                        fetchVideoCards,
//                      }) => {
//   // 카드 삭제 처리
//   const handleDelete = async () => {
//     try {
//       let response;
//       const constructorId = localStorage.getItem("userId");
//
//       // 카드 타입에 따라 다르게 처리
//       if (card.type === "default") {
//         const formData = new FormData();
//         formData.append("constructorId", constructorId);
//         formData.append("cardName", card.imgName);
//
//         response = await axios.post(
//             `${process.env.REACT_APP_SERVER_IP}/default-card/delete`,
//             formData,
//             {
//               headers: {
//                 "Content-Type": "multipart/form-data",
//               },
//             }
//         );
//         if (response.status === 200) {
//           alert("기본 카드 삭제 성공!");
//           fetchAACCards();
//           closeModal();
//         } else {
//           throw new Error("Unexpected response status");
//         }
//       } else if (card.type === "aac") {
//         const formData = new FormData();
//         formData.append("cardId", card.id); // cardId를 추가
//
//         const response = await axios.post(
//             `${process.env.REACT_APP_SERVER_IP}/aac/delete`,
//             formData,
//             {
//               headers: {
//                 "Content-Type": "multipart/form-data",
//               },
//             }
//         );
//
//         if (response.status === 200) {
//           alert("AAC 카드 삭제 성공!");
//           fetchAACCards();
//           setTimeout(closeModal, 200);
//         } else {
//           throw new Error("Unexpected response status for AAC");
//         }
//       } else if (card.type === "video") {
//         const formData = new FormData();
//         formData.append("cardId", card.id); // cardId를 추가
//
//         console.log("Deleting video card with the following data:", {
//           cardId: card.id, // cardId 로그 추가
//         });
//
//         const response = await axios.post(
//             `${process.env.REACT_APP_SERVER_IP}/video/delete`,
//             formData,
//             {
//               headers: {
//                 "Content-Type": "multipart/form-data",
//               },
//             }
//         );
//         if (response.status === 200) {
//           alert("비디오 삭제 성공!");
//           fetchVideoCards();
//           closeModal();
//         } else {
//           throw new Error("Unexpected response status for video");
//         }
//       }
//     } catch (error) {
//       console.error("삭제 중 오류 발생:", error);
//       alert("카드 삭제 실패!");
//     }
//   };
//
//   return (
//       <DeleteModalBg>
//         <DeleteModalBox>
//           <DeleteModalCloseButton onClick={closeModal}>
//             <img src={xButton} alt="Close" />
//           </DeleteModalCloseButton>
//           <h1>정말 삭제하시겠습니까?</h1>
//           <p>확인을 누르면 카드가 삭제됩니다.</p>
//           <div>
//             <DeleteModalButtonC onClick={closeModal}>취소</DeleteModalButtonC>
//             <DeleteModalButton onClick={handleDelete}>확인</DeleteModalButton>
//           </div>
//         </DeleteModalBox>
//       </DeleteModalBg>
//   );
// };
//
// export default DeleteModal;
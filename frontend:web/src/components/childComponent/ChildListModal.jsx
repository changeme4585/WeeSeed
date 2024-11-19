/*아동 목록 출력 모달 창*/
import React, { useState, useEffect, useContext } from "react";
import styled from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import moreInfoIcon from "../../images/svg/화살표_아래.svg";
import selectChildIcon from "../../images/svg/체크.svg"; //아동 선택
import { ChildContext } from "../../index";

const ModalBg = styled.div`
  display: flex;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background-color: transparent;
`;

const ModalBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 600px;
  max-height: 80vh; /* 최대 높이 설정 */
  background-color: var(--light-pink);
  border-radius: 15px;
  position: relative;
  box-shadow: 1px 2px 4px var(--gray);
  overflow-y: auto; /* 스크롤 가능하도록 설정 */

  h2 {
    font-family: "Pretendard-Bold";
  }
  p {
    padding: 10px;
    font-family: "Pretendard-Regular";
  }
`;

const CloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const ChildItem = styled.div`
  background-color: var(--pink);
  margin-bottom: 20px;
  padding: 8px 1px;
  padding-bottom: 0px;
  border-radius: 5px;
  max-width: 600px;
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;
`;

const ChildHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  padding: 7px;
  width: 500px;
`;

const MoreInfoIcon = styled.img`
  margin-right: 10px;
  cursor: pointer;
`;

const ChildName = styled.span`
  font-family: "Pretendard-Bold";
  margin-right: auto;
`;

const ChildInfo = styled.div`
  display: flex;
  align-items: center;
`;

const ChildBirth = styled.span`
  font-size: 13px;
  font-family: "Pretendard-Regular";
  margin-right: 20px;
`;

const SelectChildIcon = styled.img`
  width: 20px;
  height: 20px;
  cursor: pointer;
`;

const ChildDetails = styled.div`
  margin: 0px 3px;
  margin-bottom: 4px;
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease;

  &.expanded {
    max-height: 200px;
  }
`;

const DetailItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 30px;
  margin-top: 3px;
  background-color: var(--light-pink);
  .cinfo {
    text-align: right;
    width: 20%;
    padding-right: 5px;
  }
`;

const ChildListModal = ({ closeModal }) => {
  const [childList, setChildList] = useState([]);
  const [expandedChildCode, setExpandedChildCode] = useState(null); // 확장된 아동 코드 상태
  const { setSelectedChild } = useContext(ChildContext);

  //로컬에서 아동 정보 불러오기
  useEffect(() => {
    const storedChild = JSON.parse(localStorage.getItem("child"));
    if (storedChild) {
      setChildList(storedChild);
    }
  }, []);

  const handleChildClick = (child) => {
    setSelectedChild(child); //클릭한 아동 코드를 onSelectChild 함수로 넘기기
    localStorage.setItem("selectedChild", JSON.stringify(child));
    localStorage.setItem("selectedChildCode", child.childCode);
    console.log("목록 모달창에서 선택한 아동 코드:", child.childCode);
    closeModal();
  };

  const toggleExpand = (childCode) => {
    setExpandedChildCode((prevCode) =>
      prevCode === childCode ? null : childCode
    );
  };

  return (
    <ModalBg>
      <ModalBox>
        <CloseButton onClick={closeModal}>
          <img src={xButton} alt="Close" />
        </CloseButton>
        <h2>아동 목록</h2>
        {childList.map((child) => (
          <div key={child.childCode}>
            <ChildItem>
              <ChildHeader onClick={() => toggleExpand(child.childCode)}>
                <MoreInfoIcon src={moreInfoIcon} alt="MoreInfo" />
                <ChildName>
                  {child.name} ({child.gender})
                </ChildName>
                <ChildInfo>
                  <ChildBirth>{child.birth}</ChildBirth>
                  <SelectChildIcon
                    src={selectChildIcon}
                    alt="SelectChild"
                    onClick={() => handleChildClick(child)}
                  />
                </ChildInfo>
              </ChildHeader>
              <ChildDetails
                className={
                  expandedChildCode === child.childCode ? "expanded" : ""
                }
              >
                <DetailItem>
                  <span>장애 등급</span>
                  <span className="cinfo">{child.grade}</span>
                </DetailItem>
                <DetailItem>
                  <span>장애 유형</span>
                  <span className="cinfo">{child.disabilityType}</span>
                </DetailItem>
              </ChildDetails>
            </ChildItem>
          </div>
        ))}
      </ModalBox>
    </ModalBg>
  );
};

export default ChildListModal;

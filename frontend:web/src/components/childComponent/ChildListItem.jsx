/*설정에서 아동 목록 출력시 띄울 아동 정보 컴포넌트*/
import React, { useState } from "react";
import moreInfoIcon from "../../images/svg/down-arrow.svg";
import selectChildIcon from "../../images/svg/link-black-version.svg"; //아동 선택
import deleteIcon from "../../images/svg/delete-icon.svg"
//import deleteChildIcon from "../../images/svg/언링크.svg";


const ChildListItem = ({ child, index, onSelectChild }) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const toggleExpand = () => {
    setIsExpanded(!isExpanded);
  };

  const handleSelectChild = (e) => {
    e.stopPropagation(); // 이벤트 전파 방지
    onSelectChild(child);
    alert("아동이 선택되었습니다!");

  };

  const styles = `
  .total-child-div {
    width: 100%;
  }
  .child-list-item {
    background-color: rgba(255, 239, 133, 1);
    padding: 0px;
    width: 100%
    box-sizing: border-box;
    overflow: hidden;
    transition: max-height 0.3s ease;
    display: block; 
    border-radius: 5px;
    margin-bottom: 20px;
  }

  .child-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    cursor: pointer;
    width: 820px;
    height: 60px;
    box-sizing: border-box;
    margin-bottom: 0px;
  }

  .more-child-info-btn {
    margin: 20px;
    cursor: pointer;
    width: 20px;
    height: 20px;
  }

  .child-name {
    font-family: "Pretendard-Bold";
    margin-right: auto;
    font-size:16px;
  }

  .child-info {
    display: flex;
    align-items: flex-end;
  }

  .child-birth {
    font-size: 16px;
    font-family: "Pretendard-Regular";
  }

  .select-child{
    width: 22px;
    height: 22px;
    cursor: pointer;
    margin-right:10px;
    margin-left:20px;
  }
  .child-details {
    display: flex; 
    flex-direction: column;
    margin-top: 0px;
    max-height: 0;
    overflow: hidden;
    transition: max-height 0.3s ease;
  }

  .child-list-item.expanded .child-details {
    max-height: 200px;
  }

  .detail-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 30px;
    background-color: rgba(247, 246, 245, 1);
    padding: 5px 10px;
  }
    .detail-item span {
    font-family: "Pretendard-Regular";
    font-size: 16px;
    margin: 0 40px;
    }
`;

  return (
    <div className="total-child-div">
      <style>{styles}</style>
      <div className={`child-list-item ${isExpanded ? "expanded" : ""}`}>
        <div className="child-header" onClick={toggleExpand}>
          <img
            src={moreInfoIcon}
            alt="MoreInfo"
            className="more-child-info-btn"
          />
          <span className="child-name">
            {child.name} ({child.gender})
          </span>
          <div className="child-info">
            <span className="child-birth">{child.birth}</span>
            <img
                src={selectChildIcon}
                alt="SelectChild"
                className="select-child"
                onClick={handleSelectChild} // 클릭하면 아동 선택
            />
          </div>
        </div>
        <div className="child-details">
          <div className="detail-item">
            <span>장애 등급</span>
            <span>{child.grade}</span>
          </div>
          <div className="detail-item">
            <span>장애 유형</span>
            <span>{child.disabilityType}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChildListItem;

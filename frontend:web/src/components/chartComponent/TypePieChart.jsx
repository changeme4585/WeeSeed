import React from "react";
import { ResponsivePie } from "@nivo/pie";
import styled from "styled-components";

const StyledPieChart = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;

  .custom-legend {
    position: absolute;
    right: 0px;
    display: flex;
    flex-direction: column;
    align-items: flex-start;

    .legend-item {
      display: flex;
      align-items: center;
      margin-bottom: 14px;

      .symbol {
        display: inline-block;
        width: 18px;
        height: 18px;
        border-radius: 50%;
        margin-right: 12px;
      }

      .label,
      .value {
        font-family: "Pretendard-Regular";
        font-size: 16px;
        color: #111111;
      }

      .label {
        flex: 1;
        min-width: 80px;
        text-align: left;
        margin-right: 10px;
      }

      .value {
        min-width: 100px;
        text-align: left;
      }
    }
  }
`;

const TypePieChart = ({ data }) => {
  if (!data) {
    return null;
  }
  console.log("유형별 원형 그래프 데이터:", data);

  // 색 지정
  const colorMapping = {
    "자폐성 장애": "#F4E097",
    "지적 장애": "#92D79E",
    "행동 장애": "#A9CDE5",
    "발음 장애": "#B1C8FE",
    "뇌병변 장애": "#F4AFEF",
  };

  // 전체 장애 유형 수 계산
  const formattedData = data.map((item) => ({
    id: item.id,
    category: item.category,
    value: item.value,
    count: item.value,
  }));

  return (
    <StyledPieChart style={{ height: 480 }}>
      <ResponsivePie
        data={formattedData}
        margin={{ top: 10, right: 260, bottom: 10, left: 0 }}
        innerRadius={0.5}
        padAngle={0}
        cornerRadius={3}
        activeOuterRadiusOffset={8}
        borderWidth={0}
        borderColor={{ from: "color", modifiers: [["darker", 0.2]] }}
        arcLinkLabelsSkipAngle={10}
        arcLinkLabelsTextColor="transparent"
        arcLinkLabelsThickness={2}
        arcLinkLabelsColor="transparent"
        arcLabelsSkipAngle={10}
        arcLabelsTextColor="transparent"
        animate={true}
        colors={({ id }) => colorMapping[id]}
        legends={[]}
      />
      <div className="custom-legend">
        {formattedData.map((d) => (
          <div className="legend-item" key={d.id}>
            <div
              className="symbol"
              style={{ backgroundColor: colorMapping[d.id] }}
            ></div>
            <div className="label">{d.category}</div>
          </div>
        ))}
      </div>
    </StyledPieChart>
  );
};

export default TypePieChart;

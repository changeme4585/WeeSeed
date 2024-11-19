import React from "react";
import { Outlet } from "react-router-dom";
import Nav from "./Nav";
import MiniNav from "./MiniNav";

const Layout = ({ isMiniNav, setIsMiniNav }) => {
  return (
    <div className="app-container">
      {isMiniNav ? (
        <MiniNav setIsMiniNav={setIsMiniNav} />
      ) : (
        <Nav setIsMiniNav={setIsMiniNav} />
      )}
      <div className="main-content">
        <Outlet />
      </div>
    </div>
  );
};

export default Layout;

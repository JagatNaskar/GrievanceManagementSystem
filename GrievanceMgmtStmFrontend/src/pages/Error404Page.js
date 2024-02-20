import React from "react";
import "../assets/css/Error404Page.css";
import { NavLink } from "react-router-dom";

const Error404Page = () => {
  return (
    <div className="error-container">
      <h1 className="errorPAge">404 - Page Not Found</h1>
      <p>Sorry, the page does not exist.</p>
      <NavLink to="/">
        <button>Go Back</button>
      </NavLink>
    </div>
  );
};

export default Error404Page;

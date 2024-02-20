import React, { useState, useEffect } from "react";
import "../assets/css/DeleteDepartment.css";
import CustomAlert from "../component/CustomAlert";
import { Link, Outlet } from "react-router-dom";
import ConfirmationBox from "../component/ConfirmationBox";
import DeleteDeptIcon from "../assets/icons/recycle-bin-icon.svg";
import DataNotFound from "../assets/img/Data_not_found.png";

function AllDepartment() {
  const [delete1, setDelete1] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [deleteState, setDeleteState] = useState("");
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [showAlert, setShowAlert] = useState(false);
  const [message, setMessage] = useState("");

  const handleShowAlert = () => {
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  const handleNextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const handlePreviousPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };
  
  const [departmentList, setDepartmentList] = useState([
    // { deptId: "", deptName: "" },
  ]);

  const getAllDept = (currentPage) => {
    fetch(`http://localhost:8080/api/dept/allDepartment/${currentPage}`)
      .then((response) => response.json())
      .then((data) => setDepartmentList(data))
      .catch((error) => console.error("Error:", error));
  };

  useEffect(() => {
    getAllDept(currentPage);
  }, [currentPage, delete1]);

  const handleConfirm = () => {
    handleDelete();
    setShowConfirmation(false);
  };

  const handleCancel = () => {
    setShowConfirmation(false);
  };

  const handleDelete = async () => {
    await fetch(`http://localhost:8080/api/dept/deleteDept/${deleteState}`, {
      method: "POST",
    })
      .then((response) => {
        if (response.ok) {
          setDepartmentList((prevDepartments) =>
            prevDepartments.filter((dept) => dept.deptName !== deleteState)
          );
          setMessage("Deleted Successfully...");
          handleShowAlert();
          setCurrentPage(0);
        } else {
          console.error("Error deleting department:", response.statusText);
        }
      })
      .catch((error) => console.error("Error deleting department:", error));
    setDelete1(false);
  };

  const openConfirmBox = (name) => {
    setShowConfirmation(true);
    setDeleteState(name);
  };

  const departmentTableRows =
    departmentList.length > 0 ? (
      departmentList.map((department, index) => (
        <tr className="row" key={index}>
          <td>{index + 1}</td>
          <td>{department.deptId}</td>
          <td>{department.deptName}</td>
          <td>
            <button
              className="delete-button"
              onClick={() => openConfirmBox(department.deptName)}
              disabled={
                localStorage.getItem("MyDeptName") === department.deptName
              }
            >
              <img
                src={DeleteDeptIcon}
                alt="Description"
                style={{ width: "48%" }}
              />
            </button>
          </td>
        </tr>
      ))
    ) : (
      <tr>
        <td colSpan="4">
          {/* <p className="Data-not-available">
            No Data available in this page...
          </p> */}
          <div className="DD-Not_Found_Image">
            <img src={DataNotFound} style={{ width: "38%" }} alt="" />
          </div>
        </td>
      </tr>
    );

  return (
    <>
      <Outlet />
      <Link to="createDept" className="add-dept-btn-link">
        <div className="DD-addDept-btn" >Add Department</div>
      </Link>
      <div className="DD-department-list">
        <table className="DD-table">
          <thead className="DD-table-head">
            <tr className="DD-table-row">
              <th>Serial No</th>
              <th>Department Id</th>
              <th>Department Name</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>{departmentTableRows}</tbody>
        </table>
        <div>
          <button
            className="paging-btn"
            onClick={handlePreviousPage}
            disabled={currentPage === 0}
          >
            Prev
          </button>
          {currentPage + 1}
          <button
            className="paging-btn"
            onClick={handleNextPage}
            disabled={5 > departmentList.length}
          >
            Next
          </button>
        </div>
        <div>
          {showConfirmation && (
            <ConfirmationBox
              message="Sure to delete this department?"
              onConfirm={handleConfirm}
              onCancel={handleCancel}
            />
          )}
          {showAlert && (
            <CustomAlert
              message={message}
              handleCloseAlert={handleCloseAlert}
            />
          )}
        </div>
      </div>
    </>
  );
}

export default AllDepartment;

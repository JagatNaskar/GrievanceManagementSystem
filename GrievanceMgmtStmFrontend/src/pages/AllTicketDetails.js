import React, { useState, useEffect } from "react";
import "../assets/css/AllTicketDetails.css";
import FullTicketModel from "../pages/FullTicketModel";
import axios from "axios";
import UpdateTicket from "../pages/UpdateTicket";
import { format } from "date-fns";
import EditIcon from "../assets/icons/edit-icon.svg";
import DataNotFound from "../assets/img/Data_not_found.png";
import ViewIcon from "../assets/icons/view-in-icon.svg";
import { useCallback } from "react";
import { Link } from "react-router-dom";
import { createTicketPath } from "../util/Paths";

const AllTicketDetails = () => {
  const statusList = ["Open", "Being_Addressed", "Resolved"];
  const [showFullDetails, setShowFullDetails] = useState(false);
  const [showEditTicket, setShowEditTicket] = useState(false);
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [editButtonsDisabled, setEditButtonsDisabled] = useState(false);
  const [activeLink, setActiveLink] = useState("dept");
  const [ticketList, setTicketList] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState("");
  const [deptWise, setDeptWise] = useState("true");
  const [OwnWise, setOwnWise] = useState("false");
  const [currentPage, setCurrentPage] = useState(0);
  const handleNextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const handlePreviousPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  const fetchData = useCallback(async () => {
    try {
      const dataToSend = {
        email: btoa(localStorage.getItem("session_user_name")),
        departmentBased: deptWise,
        assignByOwn: OwnWise,
        filterStatus: selectedStatus,
        pageNumber: currentPage,
      };

      const response = await axios.post(
        "http://localhost:8080/api/tickets/getAllTicket",
        dataToSend
      );

      if (response.status !== 200) {
        throw new Error("Network response was not ok");
      }

      const data = response.data;
      setTicketList(data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  }, [OwnWise, deptWise, selectedStatus, currentPage]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const openEditModel = (ticket) => {
    setSelectedTicket(ticket);
    setShowEditTicket(true);
  };
  const openModal = (ticket) => {
    setSelectedTicket(ticket);
    setShowFullDetails(true);
  };

  const closeModal = () => {
    setShowFullDetails(false);
  };
  const closeEditModel = () => {
    setShowEditTicket(false);
  };

  const handleEdit = (ticket) => {
    setSelectedTicket(ticket);
    openEditModel(ticket);
  };

  const handleFilterChange = (e) => {
    setCurrentPage(0);
    setSelectedStatus(e.target.value);
  };

  const AdminRole = localStorage.getItem("Admin_Role");

  return (
    <>
      <Link to={createTicketPath} className="add-dept-btn-link">
        <div className="AT-addticket-btn">Add Ticket</div>
      </Link>
      <div className="full-details">
        <div className="all-button">
          {AdminRole === "admin" && (
            <button
              className={
                activeLink === "allTicket" ? "active" : "AT-allTickets"
              }
              onClick={() => {
                setCurrentPage(0);
                setOwnWise("false");
                setDeptWise("false");
                setSelectedStatus("Select status");
                setEditButtonsDisabled(true);
                setActiveLink("allTicket");
              }}
            >
              All Tickets
            </button>
          )}

          <button
            className={activeLink === "dept" ? "active" : "AT-deptTickets"}
            onClick={() => {
              setCurrentPage(0);
              setDeptWise("true");
              setOwnWise("false");
              setSelectedStatus("Select status");
              setEditButtonsDisabled(false);
              setActiveLink("dept");
            }}
          >
            Dept Based
          </button>

          <button
            className={activeLink === "ticket" ? "active" : "AT-myTickets"}
            onClick={() => {
              setCurrentPage(0);
              setDeptWise("false");
              setOwnWise("true");
              setSelectedStatus("Select status");
              setEditButtonsDisabled(false);
              setActiveLink("ticket");
            }}
          >
            My Tickets
          </button>

          <select
            id="statusFilter"
            className="statusFilter"
            name="filter"
            value={selectedStatus}
            onChange={handleFilterChange}
          >
            <option value="">Select status</option>
            {statusList.map((e) => (
              <option key={e} value={e}>
                {e}
              </option>
            ))}
          </select>
        </div>
        {ticketList.length > 0 ? (
          <table>
            <thead>
              <tr>
                <th>Ticket Id</th>
                <th>Department</th>
                <th>Ticket Status</th>
                <th>Created By</th>
                <th>Last Updated</th>
                <th>Edit</th>
                <th>View</th>
              </tr>
            </thead>
            <tbody>
              {ticketList.map((ticket) => {
                const formattedDateTime = format(
                  new Date(ticket.updationTime),
                  "EEE, d MMM , yyyy  h:mm a"
                );

                return (
                  <tr key={ticket.ticketId} className="ticketrow">
                    <td>{ticket.ticketId}</td>
                    <td>{ticket.departmentName}</td>
                    <td>{ticket.ticketStatus}</td>
                    <td>{ticket.createdBy}</td>
                    <td>{formattedDateTime}</td>

                    <td>
                      <button
                        className="edit-btn"
                        onClick={() => handleEdit(ticket)}
                      >
                        <img
                          src={EditIcon}
                          alt="Description"
                          style={{ width: "60%" }}
                        />
                      </button>
                    </td>
                    <td>
                      <button
                        className="expand-btn"
                        onClick={() => openModal(ticket)}
                      >
                        <img
                          src={ViewIcon}
                          alt="Description"
                          style={{ width: "60%" }}
                        />
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        ) : (
          <div className="Not_Found_Image">
            <img src={DataNotFound} style={{ width: "45%" }} alt="" />
          </div>
        )}
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
            disabled={5 > ticketList.length}
          >
            Next
          </button>
        </div>

        {showEditTicket && (
          <UpdateTicket
            id={selectedTicket.ticketId}
            onClose={closeEditModel}
            editButtonsDisabled={editButtonsDisabled}
          />
        )}

        {showFullDetails && (
          <FullTicketModel ticket={selectedTicket} onClose={closeModal} />
        )}
      </div>
    </>
  );
};

export default AllTicketDetails;

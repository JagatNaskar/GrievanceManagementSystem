import "./App.css";
import Login from "./pages/Login";
import {
  Route,
  Routes,
  useNavigate,
} from "react-router-dom";
import AdminHome from "./pages/AdminHome";
import NewUser from "./pages/NewUser";
import Ticket from "./pages/Ticket";
import AdminSidebar from "./pages/AdminSidebar";
import NewDepartment from "./pages/NewDepartment";
import DeleteUser from "./pages/DeleteUser";
import DeleteDepartment from "./pages/DeleteDepartment";
import PrivateRoute from "./pages/PrivateRoute"; 
import Error404Page from "./pages/Error404Page";
import UpdateTicket from "./pages/UpdateTicket";
import AllTicketDetails from "./pages/AllTicketDetails";
import PasswordChange from "./pages/PasswordChange";
import { useEffect } from "react";

function App() {

  let nevigatee = useNavigate();

  useEffect(() => {
    if (localStorage.getItem("LoggendIn22") === "true") {
      nevigatee("/tickets")
    }
  }, [nevigatee]);
  
  useEffect(() => {
    const storedRoute = localStorage.getItem('currentRoute');
    if (storedRoute) {
      localStorage.removeItem('currentRoute'); // Remove stored route
      nevigatee(storedRoute); // Navigate to the stored route
    }
  }, [nevigatee]);
  return (
    <div className="App">
      {
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="login" element={<Login />} />
          <Route element={<PrivateRoute />}>
            {localStorage.getItem("finalPassword_set") === "false" && (
            <Route path="/changePassword" element={<PasswordChange />} />
            )
          }
            <Route element={<AdminSidebar />}>
              <Route path="adminProfile" element={<AdminHome />} />
              <Route path="users/addUser" element={<NewUser />} />
              <Route path="users" element={<DeleteUser />} />
              <Route path="viewDept" element={<DeleteDepartment />}>
                <Route path="createDept" element={<NewDepartment />} />
              </Route>
              <Route path="tickets/createTicket" element={<Ticket />} />
              <Route path="changePassword" element={<PasswordChange />} />
              <Route path="tickets" element={<AllTicketDetails />} />
              <Route path="tickets/createTicket" element={<Ticket />} />
              <Route path="tickets/update-ticket" element={<UpdateTicket />} />
              <Route
                path="allFeedback"
                element={<div>All Feedback & Greviance Page</div>}
              />
            </Route>
          </Route>
          <Route path="*" element={<Error404Page />} />
        </Routes>
      }
    </div>
  );
}

export default App;

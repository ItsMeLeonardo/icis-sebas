package com.proyect.servlets.UClient;

import com.proyect.modelsDAO.UClient.ClientDAO;
import com.proyect.modelsDAO.General.UserDAO;
import com.proyect.modelsDTO.UClient.Client;
import com.proyect.modelsDTO.UClient.District;
import com.proyect.modelsDTO.General.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SCRegister", value = "/SCRegister")
public class SCRegister extends HttpServlet {

    List<User> users = new ArrayList<>();
    User user = new User();
    Client client = new Client();
    District district = new District();
    UserDAO udao = new UserDAO();
    ClientDAO cdao = new ClientDAO();
    Boolean error = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action.equals("register")) {
            this.saveAccount(request, response);
        }
    }

    public void saveAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer idDistrict = 1;
        int countTrue = 0;
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String phone = request.getParameter("phone");
        String docIdentity = request.getParameter("docIdentity");

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        users = udao.list();
        Object[] emails = users.toArray();
        try {
            for (int i = 0; i < users.size(); i++) {
                Boolean em = null;
                if (em = emails[i].toString().contains(email)) {
                    em = true;
                    countTrue++;
                } else {
                    em = false;
                }
            }

            if (countTrue > 0) {
                error = true;
                request.setAttribute("errorReg", error);
                request.setAttribute("errorRegister", "El email ingresado ya ha sido usado");//jsp register
                request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
            } else if (countTrue == 0) {
                Integer idUser = null;
                user.setIdUser(idUser);
                user.setEmail(email);
                user.setPassword(password);
                user.setAvatar("client.png");
                user.setFlag(0);
                udao.save(user);

                idUser = udao.getLastIdUser();
                user.setIdUser(idUser);
                district.setIdDistrict(idDistrict);

                client.setUsername(username);
                client.setName(name);
                client.setSurname(surname);
                client.setPhone(phone);
                client.setDocIdentity(docIdentity);
                client.setUser(user);
                client.setDistrict(district);
                cdao.save(client);
            }
            response.sendRedirect("views/user/login.jsp");
        } catch (Exception ignored) {
        }
    }
}
package com.example.smartcity.controller;

import com.example.smartcity.model.BookingBean;
import com.example.smartcity.model.ParkingBean;
import com.example.smartcity.model.ParkingDao;
import com.example.smartcity.service.BookingService;
import com.example.smartcity.service.Factory.*;
import com.example.smartcity.service.ParkingService;
import com.example.smartcity.service.Strategy.PaypalStrategy;
import com.example.smartcity.service.Strategy.PaymentStrategy;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "PayPalServlet", value = "/PayPalServlet")
public class PayPalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");


        String email = request.getParameter("email");
        String emailPP = request.getParameter("emailPP");
        String passwordPP = request.getParameter("passwordPP");

        HttpSession session = request.getSession(false);
        if ( session == null ) {
            session.setAttribute("isLog",0);
            request.getRequestDispatcher("login.jsp").forward(request,response);
        } else {
            BookingBean bookingBean = (BookingBean) session.getAttribute("bookingBean");

            int id = bookingBean.getId_parcheggio();
            ParkingBean parkingBean = ParkingService.getParkingBean(id);


            PaymentStrategy paymentMethod = new PaypalStrategy(emailPP, passwordPP);

            String tipoVeicolo = bookingBean.getTipoVeicolo();
            System.out.println("veicolo " + tipoVeicolo);

            switch (tipoVeicolo){
                case "Auto":
                    if (paymentMethod.pay(bookingBean.getPrezzo())){
                        FactoryPosto factoryAuto = new FactoryPostoAuto();
                        Posto auto = factoryAuto.getPosto(id, parkingBean);

                        //Inserisco la prenotazione
                        BookingService.Booking(bookingBean);
                        session.setAttribute("email", email);
                        request.setAttribute("parkingBean",parkingBean);
                        session.setAttribute("bookingBean",bookingBean);
                        request.getRequestDispatcher("thankYouPage.jsp").forward(request,response);
                    }
                    else
                        request.getRequestDispatcher("errorPage.jsp").forward(request,response);

                    break;
                case "Furgone":

                    if (paymentMethod.pay(bookingBean.getPrezzo())){
                        FactoryPosto factoryFurgone = new FactoryPostoFurgone();
                        Posto furgone = factoryFurgone.getPosto(id, parkingBean);

                        //Inserisco la prenotazione
                        BookingService.Booking(bookingBean);
                        session.setAttribute("email", email);

                        request.setAttribute("parkingBean",parkingBean);
                        session.setAttribute("bookingBean",bookingBean);
                        request.getRequestDispatcher("thankYouPage.jsp").forward(request,response);
                    }
                    else
                        request.getRequestDispatcher("errorPage.jsp").forward(request,response);

                    break;
                case "Moto":
                    if (paymentMethod.pay(bookingBean.getPrezzo())){
                        paymentMethod.pay(bookingBean.getPrezzo());
                        FactoryPosto factoryMoto = new FactoryPostoMoto();
                        Posto moto = factoryMoto.getPosto(id, parkingBean);

                        //Inserisco la prenotazione
                        BookingService.Booking(bookingBean);
                        session.setAttribute("email", email);

                        request.setAttribute("parkingBean",parkingBean);
                        session.setAttribute("bookingBean",bookingBean);
                        request.getRequestDispatcher("thankYouPage.jsp").forward(request,response);
                    }
                    else
                        request.getRequestDispatcher("errorPage.jsp").forward(request,response);

                    break;
                default:
                    request.getRequestDispatcher("errorPage.jsp").forward(request,response);
                    break;
            }

        }

    }
}
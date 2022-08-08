package bg.softuni.creddit.web.interceptor;

import bg.softuni.creddit.model.validation.SecurityCheck;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Component
public class SecurityCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if(handler instanceof HandlerMethod
                && request.getMethod().equals("POST")
                && ((HandlerMethod) handler).hasMethodAnnotation(SecurityCheck.class)) {

            String userAnswer = request.getParameter("answer");

            if(userAnswer == null || userAnswer.isEmpty()) {
                response.setStatus(403);
                return false;
            }

            userAnswer = userAnswer.toLowerCase().trim();

            Integer number = (Integer) session.getAttribute("number");

            if(number == null) {
                return false;
            }

            boolean answerIsWrong = number % 2 == 0
                    ? userAnswer.equals("odd")
                    : userAnswer.equals("even");

            if(answerIsWrong) {
                return false;
            }

            session.removeAttribute("number");
        }

        if(session.getAttribute("number") == null) {
            Integer number = new Random().nextInt(100);
            session.setAttribute("number", number);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(request.getMethod().equals("GET") && modelAndView != null) {
            modelAndView.addObject("number", request.getSession().getAttribute("number"));
        }
    }
}

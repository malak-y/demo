package API;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import EJB.Calc;

@Stateless
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class calcservice {
     @PersistenceContext
	private EntityManager entityManager;
     
    @POST
    @Path("calc")
    //@Path("post")
    public Response createCalculation(Calc calc) {
        try {
            int result = performCalculation(calc.getNumber1(), calc.getNumber2(), calc.getOperation());
             
			entityManager.persist(calc);
            return Response.ok().entity("{\"Result\": " + result + "}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("An error occurred: " + e.getMessage()).build();
        }
    }

    // Helper method to perform calculations
    private int performCalculation(int num1, int num2, String operation) {
        switch (operation) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 != 0)
                    return num1 / num2;
                else
                    throw new ArithmeticException("Division by zero");
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }
    @GET
    @Path("calculations")
    public Response getAllCalculations() {
        try {
            TypedQuery<Calc> query = entityManager.createQuery("SELECT c FROM Calc c", Calc.class);
            List<Calc> calculations = query.getResultList();
            return Response.ok().entity(calculations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("An error occurred: " + e.getMessage()).build();
        }
    }
}

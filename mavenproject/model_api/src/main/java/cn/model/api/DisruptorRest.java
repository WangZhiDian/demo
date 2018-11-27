package cn.model.api;

import cn.model.maven.service.disruptor.simpledisruptor.service.DisruptorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by wangdian05 on 2018/7/2.
 */
@Slf4j
@Path("/disruptor")
public class DisruptorRest {

    @Autowired
    DisruptorService disruptorService;

    @GET
    @Path("/startDisruptor")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean startDisruptor()
    {
        // http://localhost:8080/model_api/rest/disruptor/startDisruptor
        boolean ret = disruptorService.startDisruptor();
        log.info("start:{}", ret);
        return ret;
//        return true;
    }

    @GET
    @Path("/publishAnEvent")
    @Produces(MediaType.APPLICATION_JSON)
    public String publishAnEvent(@Context HttpServletRequest request, String jsonData)
    {
        // http://localhost:8080/model_api/rest/disruptor/publishAnEvent?name=testwang
        System.out.println(request.getParameter("parm1") + " | " + jsonData);
        String name = request.getParameter("name");
        String ret = disruptorService.publishAnEvent(name);

        return ret;
    }

    @GET
    @Path("/publishAnEvent2")
    @Produces(MediaType.APPLICATION_JSON)
    public String publishAnEvent2(@Context HttpServletRequest request,String jsonData)
    {
        // http://localhost:8080/model_api/rest/disruptor/publishAnEvent2?name=testwang
        System.out.println(request.getParameter("parm1") + " | " + jsonData);
        String name = request.getParameter("name");
        disruptorService.publishEvent2(name);

        return "";
    }

}

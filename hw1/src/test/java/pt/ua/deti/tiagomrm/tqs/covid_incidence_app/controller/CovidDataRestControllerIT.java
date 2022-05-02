package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.Hw1Application;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Hw1Application.class)
@AutoConfigureMockMvc
class CovidDataRestControllerIT {


    @Autowired
    private MockMvc mvc;

    @Test
    void getGlobalReportIT() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get("/api/report?date=2022-05-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2022-05-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.region").value("Global"));
    }

    @Test
    void getRegionalReportIT() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get("/api/report?region=Portugal&date=2022-05-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2022-05-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.region").value("Portugal"));
    }

}

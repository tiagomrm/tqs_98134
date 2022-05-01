package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CovidAPIByAPISPORTSTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @InjectMocks
    CovidAPIByAPISPORTS api = new CovidAPIByAPISPORTS();

    @Mock
    BasicHttpClient httpClient;

    @Mock
    HttpResponse<JsonNode> mockHttpResponse;
    JsonNode mockedRegionsJsonNode = new JsonNode(
            "{" +
                    "\"get\":\"countries\"," +
                    "\"parameters\":[]," +
                    "\"errors\":[]," +
                    "\"results\":233," +
                    "\"response\":[" +
                        "\"Afghanistan\"," +
                        "\"Albania\"," +
                        "\"Algeria\"," +
                        "\"Andorra\"," +
                        "\"Puerto-Rico\"" +
                    "]}"
    );

    JsonNode mockedRegionalReportJsonNode = new JsonNode(
            "{\"get\":\"history\",\"parameters\":{\"country\":\"Portugal\",\"day\":\"2022-04-30\"},\"errors\":[],\"results\":3,\"response\":[{\"continent\":\"Europe\",\"country\":\"Portugal\",\"population\":10142559,\"cases\":{\"new\":null,\"active\":null,\"critical\":61,\"recovered\":null,\"1M_pop\":\"379963\",\"total\":3853800},\"deaths\":{\"new\":null,\"1M_pop\":\"2197\",\"total\":22280},\"tests\":{\"1M_pop\":\"4063908\",\"total\":41218424},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T15:45:02+00:00\"},{\"continent\":\"Europe\",\"country\":\"Portugal\",\"population\":10142559,\"cases\":{\"new\":null,\"active\":null,\"critical\":61,\"recovered\":null,\"1M_pop\":\"373845\",\"total\":3791744},\"deaths\":{\"new\":null,\"1M_pop\":\"2185\",\"total\":22162},\"tests\":{\"1M_pop\":\"4063908\",\"total\":41218424},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T02:30:03+00:00\"},{\"continent\":\"Europe\",\"country\":\"Portugal\",\"population\":10142640,\"cases\":{\"new\":null,\"active\":null,\"critical\":61,\"recovered\":null,\"1M_pop\":\"373842\",\"total\":3791744},\"deaths\":{\"new\":null,\"1M_pop\":\"2185\",\"total\":22162},\"tests\":{\"1M_pop\":\"4063875\",\"total\":41218424},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T00:00:03+00:00\"}]}"
    );

    JsonNode mockedGlobalReportJsonNode = new JsonNode(
            "{\"get\":\"history\",\"parameters\":{\"country\":\"All\",\"day\":\"2022-04-30\"},\"errors\":[],\"results\":59,\"response\":[{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+414237\",\"active\":39877459,\"critical\":41136,\"recovered\":467081755,\"1M_pop\":\"65841\",\"total\":513219474},\"deaths\":{\"new\":\"+1251\",\"1M_pop\":\"803.1\",\"total\":6260260},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T23:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+414204\",\"active\":39877452,\"critical\":41138,\"recovered\":467081738,\"1M_pop\":\"65841\",\"total\":513219441},\"deaths\":{\"new\":\"+1242\",\"1M_pop\":\"803.1\",\"total\":6260251},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T23:30:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+413449\",\"active\":39877223,\"critical\":41138,\"recovered\":467081214,\"1M_pop\":\"65841\",\"total\":513218686},\"deaths\":{\"new\":\"+1240\",\"1M_pop\":\"803.1\",\"total\":6260249},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T23:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+413017\",\"active\":39877259,\"critical\":41138,\"recovered\":467080754,\"1M_pop\":\"65841\",\"total\":513218254},\"deaths\":{\"new\":\"+1232\",\"1M_pop\":\"803.1\",\"total\":6260241},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T23:00:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+412160\",\"active\":39876276,\"critical\":41138,\"recovered\":467080886,\"1M_pop\":\"65841\",\"total\":513217397},\"deaths\":{\"new\":\"+1226\",\"1M_pop\":\"803.1\",\"total\":6260235},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T22:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+412062\",\"active\":39876181,\"critical\":41138,\"recovered\":467080886,\"1M_pop\":\"65841\",\"total\":513217299},\"deaths\":{\"new\":\"+1223\",\"1M_pop\":\"803.1\",\"total\":6260232},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T22:30:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+410673\",\"active\":39878416,\"critical\":41131,\"recovered\":467077314,\"1M_pop\":\"65841\",\"total\":513215910},\"deaths\":{\"new\":\"+1171\",\"1M_pop\":\"803.1\",\"total\":6260180},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T22:15:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+410320\",\"active\":39866615,\"critical\":41131,\"recovered\":467088763,\"1M_pop\":\"65841\",\"total\":513215557},\"deaths\":{\"new\":\"+1170\",\"1M_pop\":\"803.1\",\"total\":6260179},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T22:00:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+392593\",\"active\":39855802,\"critical\":41140,\"recovered\":467081873,\"1M_pop\":\"65838\",\"total\":513197830},\"deaths\":{\"new\":\"+1146\",\"1M_pop\":\"803.1\",\"total\":6260155},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T21:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+392254\",\"active\":39855882,\"critical\":41140,\"recovered\":467081454,\"1M_pop\":\"65838\",\"total\":513197491},\"deaths\":{\"new\":\"+1146\",\"1M_pop\":\"803.1\",\"total\":6260155},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T21:00:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+387979\",\"active\":39852044,\"critical\":41140,\"recovered\":467081019,\"1M_pop\":\"65838\",\"total\":513193216},\"deaths\":{\"new\":\"+1144\",\"1M_pop\":\"803.1\",\"total\":6260153},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T20:15:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+338447\",\"active\":39910275,\"critical\":41140,\"recovered\":466973317,\"1M_pop\":\"65832\",\"total\":513143684},\"deaths\":{\"new\":\"+1083\",\"1M_pop\":\"803.1\",\"total\":6260092},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T20:00:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+316977\",\"active\":39906986,\"critical\":41131,\"recovered\":466955172,\"1M_pop\":\"65829\",\"total\":513122214},\"deaths\":{\"new\":\"+1047\",\"1M_pop\":\"803.1\",\"total\":6260056},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T19:45:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+306947\",\"active\":39898661,\"critical\":41130,\"recovered\":466953490,\"1M_pop\":\"65828\",\"total\":513112184},\"deaths\":{\"new\":\"+1024\",\"1M_pop\":\"803.1\",\"total\":6260033},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T19:30:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+304417\",\"active\":39896214,\"critical\":41130,\"recovered\":466953420,\"1M_pop\":\"65827\",\"total\":513109654},\"deaths\":{\"new\":\"+1011\",\"1M_pop\":\"803.1\",\"total\":6260020},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T17:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+250717\",\"active\":39913914,\"critical\":41135,\"recovered\":466881735,\"1M_pop\":\"65820\",\"total\":513055539},\"deaths\":{\"new\":\"+881\",\"1M_pop\":\"803.1\",\"total\":6259890},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T15:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+241401\",\"active\":39917889,\"critical\":41153,\"recovered\":466868508,\"1M_pop\":\"65819\",\"total\":513046223},\"deaths\":{\"new\":\"+817\",\"1M_pop\":\"803.1\",\"total\":6259826},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T15:30:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+185009\",\"active\":39861864,\"critical\":41153,\"recovered\":466868247,\"1M_pop\":\"65812\",\"total\":512989831},\"deaths\":{\"new\":\"+711\",\"1M_pop\":\"803.1\",\"total\":6259720},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T15:15:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+184556\",\"active\":39861460,\"critical\":41153,\"recovered\":466868202,\"1M_pop\":\"65812\",\"total\":512989378},\"deaths\":{\"new\":\"+707\",\"1M_pop\":\"803.1\",\"total\":6259716},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T13:30:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+184446\",\"active\":39861551,\"critical\":41154,\"recovered\":466868001,\"1M_pop\":\"65812\",\"total\":512989268},\"deaths\":{\"new\":\"+707\",\"1M_pop\":\"803.1\",\"total\":6259716},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T13:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+184446\",\"active\":39864113,\"critical\":41154,\"recovered\":466865439,\"1M_pop\":\"65812\",\"total\":512989268},\"deaths\":{\"new\":\"+707\",\"1M_pop\":\"803.1\",\"total\":6259716},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T13:00:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+184039\",\"active\":39863797,\"critical\":41154,\"recovered\":466865358,\"1M_pop\":\"65812\",\"total\":512988861},\"deaths\":{\"new\":\"+697\",\"1M_pop\":\"803.1\",\"total\":6259706},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T12:45:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+184020\",\"active\":39863800,\"critical\":41154,\"recovered\":466865358,\"1M_pop\":\"65812\",\"total\":512988861},\"deaths\":{\"new\":\"+694\",\"1M_pop\":\"803.1\",\"total\":6259703},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T12:30:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+183055\",\"active\":39864387,\"critical\":41154,\"recovered\":466863781,\"1M_pop\":\"65812\",\"total\":512987871},\"deaths\":{\"new\":\"+694\",\"1M_pop\":\"803.1\",\"total\":6259703},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T12:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+182122\",\"active\":39864916,\"critical\":41154,\"recovered\":466862330,\"1M_pop\":\"65811\",\"total\":512986938},\"deaths\":{\"new\":\"+683\",\"1M_pop\":\"803.1\",\"total\":6259692},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T12:00:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+177013\",\"active\":39876537,\"critical\":41273,\"recovered\":466845603,\"1M_pop\":\"65811\",\"total\":512981829},\"deaths\":{\"new\":\"+680\",\"1M_pop\":\"803.1\",\"total\":6259689},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T11:45:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+176216\",\"active\":39876778,\"critical\":41273,\"recovered\":466844571,\"1M_pop\":\"65811\",\"total\":512981032},\"deaths\":{\"new\":\"+674\",\"1M_pop\":\"803.1\",\"total\":6259683},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T11:30:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+175976\",\"active\":39878002,\"critical\":41273,\"recovered\":466843143,\"1M_pop\":\"65811\",\"total\":512980792},\"deaths\":{\"new\":\"+638\",\"1M_pop\":\"803.1\",\"total\":6259647},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T11:00:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+174692\",\"active\":39877401,\"critical\":41275,\"recovered\":466842468,\"1M_pop\":\"65810\",\"total\":512979508},\"deaths\":{\"new\":\"+630\",\"1M_pop\":\"803.1\",\"total\":6259639},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T10:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+173831\",\"active\":39885428,\"critical\":41357,\"recovered\":466833608,\"1M_pop\":\"65810\",\"total\":512978647},\"deaths\":{\"new\":\"+602\",\"1M_pop\":\"803.0\",\"total\":6259611},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T10:30:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+158701\",\"active\":39870298,\"critical\":41357,\"recovered\":466833608,\"1M_pop\":\"65808\",\"total\":512963517},\"deaths\":{\"new\":\"+602\",\"1M_pop\":\"803.0\",\"total\":6259611},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T10:00:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+157463\",\"active\":39869824,\"critical\":41358,\"recovered\":466832660,\"1M_pop\":\"65808\",\"total\":512962077},\"deaths\":{\"new\":\"+584\",\"1M_pop\":\"803.0\",\"total\":6259593},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T09:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+157457\",\"active\":39869818,\"critical\":41358,\"recovered\":466832660,\"1M_pop\":\"65808\",\"total\":512962071},\"deaths\":{\"new\":\"+584\",\"1M_pop\":\"803.0\",\"total\":6259593},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T08:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+150094\",\"active\":39873634,\"critical\":41358,\"recovered\":466821638,\"1M_pop\":\"65807\",\"total\":512954708},\"deaths\":{\"new\":\"+427\",\"1M_pop\":\"803.0\",\"total\":6259436},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T08:30:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+150029\",\"active\":39873504,\"critical\":41358,\"recovered\":466821638,\"1M_pop\":\"65807\",\"total\":512954643},\"deaths\":{\"new\":\"+492\",\"1M_pop\":\"803.0\",\"total\":6259501},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T08:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+143897\",\"active\":39876262,\"critical\":41370,\"recovered\":466812780,\"1M_pop\":\"65807\",\"total\":512948511},\"deaths\":{\"new\":\"+460\",\"1M_pop\":\"803.0\",\"total\":6259469},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T08:00:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+143788\",\"active\":39876581,\"critical\":41371,\"recovered\":466812352,\"1M_pop\":\"65806\",\"total\":512948402},\"deaths\":{\"new\":\"+460\",\"1M_pop\":\"803.0\",\"total\":6259469},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T07:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+143786\",\"active\":39876591,\"critical\":41371,\"recovered\":466812340,\"1M_pop\":\"65806\",\"total\":512948400},\"deaths\":{\"new\":\"+460\",\"1M_pop\":\"803.0\",\"total\":6259469},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T07:30:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+143761\",\"active\":39876567,\"critical\":41371,\"recovered\":466812339,\"1M_pop\":\"65806\",\"total\":512948375},\"deaths\":{\"new\":\"+460\",\"1M_pop\":\"803.0\",\"total\":6259469},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T07:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+143257\",\"active\":39877182,\"critical\":41365,\"recovered\":466811208,\"1M_pop\":\"65806\",\"total\":512947855},\"deaths\":{\"new\":\"+458\",\"1M_pop\":\"803.0\",\"total\":6259465},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T07:00:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+143104\",\"active\":39877266,\"critical\":41365,\"recovered\":466810971,\"1M_pop\":\"65806\",\"total\":512947702},\"deaths\":{\"new\":\"+458\",\"1M_pop\":\"803.0\",\"total\":6259465},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T06:45:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+141395\",\"active\":39875694,\"critical\":41369,\"recovered\":466810841,\"1M_pop\":\"65806\",\"total\":512945987},\"deaths\":{\"new\":\"+449\",\"1M_pop\":\"803.0\",\"total\":6259452},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T06:15:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+129560\",\"active\":39908604,\"critical\":41363,\"recovered\":466766110,\"1M_pop\":\"65805\",\"total\":512934152},\"deaths\":{\"new\":\"+435\",\"1M_pop\":\"803.0\",\"total\":6259438},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T05:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+129559\",\"active\":39988903,\"critical\":41363,\"recovered\":466685810,\"1M_pop\":\"65805\",\"total\":512934151},\"deaths\":{\"new\":\"+435\",\"1M_pop\":\"803.0\",\"total\":6259438},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T05:00:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+128546\",\"active\":39988607,\"critical\":41363,\"recovered\":466685133,\"1M_pop\":\"65805\",\"total\":512933138},\"deaths\":{\"new\":\"+395\",\"1M_pop\":\"803.0\",\"total\":6259398},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T03:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+128546\",\"active\":39989521,\"critical\":41363,\"recovered\":466682378,\"1M_pop\":\"65804\",\"total\":512931247},\"deaths\":{\"new\":\"+395\",\"1M_pop\":\"803.0\",\"total\":6259348},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T03:30:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+128546\",\"active\":39988904,\"critical\":41363,\"recovered\":466682378,\"1M_pop\":\"65804\",\"total\":512930618},\"deaths\":{\"new\":\"+395\",\"1M_pop\":\"803.0\",\"total\":6259336},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T03:15:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+92143\",\"active\":39998889,\"critical\":41372,\"recovered\":466636097,\"1M_pop\":\"65800\",\"total\":512894215},\"deaths\":{\"new\":\"+288\",\"1M_pop\":\"803.0\",\"total\":6259229},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T03:00:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+92143\",\"active\":39998468,\"critical\":41375,\"recovered\":466636097,\"1M_pop\":\"65799\",\"total\":512893794},\"deaths\":{\"new\":\"+288\",\"1M_pop\":\"803.0\",\"total\":6259229},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T02:45:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+92143\",\"active\":39936530,\"critical\":41375,\"recovered\":466636097,\"1M_pop\":\"65792\",\"total\":512831738},\"deaths\":{\"new\":\"+288\",\"1M_pop\":\"803.0\",\"total\":6259111},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T02:30:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+92143\",\"active\":39936845,\"critical\":41375,\"recovered\":466634879,\"1M_pop\":\"65791\",\"total\":512830834},\"deaths\":{\"new\":\"+288\",\"1M_pop\":\"803.0\",\"total\":6259110},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T02:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+567481\",\"active\":39873818,\"critical\":41374,\"recovered\":466606018,\"1M_pop\":\"65780\",\"total\":512738657},\"deaths\":{\"new\":\"+2249\",\"1M_pop\":\"802.9\",\"total\":6258821},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T01:45:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+567192\",\"active\":39871839,\"critical\":41374,\"recovered\":466606018,\"1M_pop\":\"65779\",\"total\":512736640},\"deaths\":{\"new\":\"+2243\",\"1M_pop\":\"802.9\",\"total\":6258783},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T01:30:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+564555\",\"active\":39875809,\"critical\":41377,\"recovered\":466599417,\"1M_pop\":\"65779\",\"total\":512734003},\"deaths\":{\"new\":\"+2237\",\"1M_pop\":\"802.9\",\"total\":6258777},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T01:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+560608\",\"active\":39871440,\"critical\":41367,\"recovered\":466597284,\"1M_pop\":\"65778\",\"total\":512727469},\"deaths\":{\"new\":\"+2233\",\"1M_pop\":\"802.9\",\"total\":6258745},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T01:00:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+560038\",\"active\":39872065,\"critical\":41362,\"recovered\":466596108,\"1M_pop\":\"65778\",\"total\":512726899},\"deaths\":{\"new\":\"+2214\",\"1M_pop\":\"802.9\",\"total\":6258726},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T00:45:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+557168\",\"active\":39869205,\"critical\":41360,\"recovered\":466596108,\"1M_pop\":\"65778\",\"total\":512724029},\"deaths\":{\"new\":\"+2204\",\"1M_pop\":\"802.9\",\"total\":6258716},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T00:30:03+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+556664\",\"active\":39867234,\"critical\":41362,\"recovered\":466595429,\"1M_pop\":\"65777\",\"total\":512721346},\"deaths\":{\"new\":\"+2204\",\"1M_pop\":\"802.9\",\"total\":6258683},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T00:15:02+00:00\"},{\"continent\":\"All\",\"country\":\"All\",\"population\":null,\"cases\":{\"new\":\"+550617\",\"active\":39866620,\"critical\":41367,\"recovered\":466590016,\"1M_pop\":\"65777\",\"total\":512715299},\"deaths\":{\"new\":\"+2184\",\"1M_pop\":\"802.9\",\"total\":6258663},\"tests\":{\"1M_pop\":null,\"total\":null},\"day\":\"2022-04-30\",\"time\":\"2022-04-30T00:00:02+00:00\"}]}"
    );


    JsonNode mockedEmptyJson = new JsonNode("{\"data\":[]}");

    @BeforeEach
    void init(){
    }

    @AfterEach
    void tearDown() {
        reset(mockHttpResponse, httpClient);
    }


    @Test
    void testGettingSuccessfulRegionalReport_thenReturnThatCovidReportWrappedByOptional() {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedRegionalReportJsonNode);
        when(httpClient.getHttpResponse("https://covid-193.p.rapidapi.com/history?country=Portugal&day=2022-04-30")).thenReturn(mockHttpResponse);

        CovidReport regionalReport = CovidReport.getRegionalCovidReport(
                "Portugal", LocalDate.parse("2022-04-30", formatter), 3853800, 0, 22280, 0
        );

        Key key = Key.getRegionalKey("Portugal", LocalDate.parse("2022-04-30", formatter));

        Optional<CovidReport> optionalReport = api.getReport(key);

        assertAll( ()-> {
            assertTrue(optionalReport.isPresent());
            assertThat(api.getReport(key).get(), equalTo(regionalReport));
        });
    }


    @Test
    void testGettingSuccessfulGlobalReport_thenReturnThatCovidReportWrappedByOptional() {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedGlobalReportJsonNode);
        when(httpClient.getHttpResponse("https://covid-193.p.rapidapi.com/history?country=All&day=2022-04-30")).thenReturn(mockHttpResponse);

        CovidReport globalReport = CovidReport.getGlobalCovidReport(
                LocalDate.parse("2022-04-30", formatter), 513219474, 414237, 6260260, 1251
        );

        Key key = Key.getGlobalKey(LocalDate.parse("2022-04-30", formatter));

        Optional<CovidReport> optionalReport = api.getReport(key);

        assertAll( ()-> {
            assertTrue(optionalReport.isPresent());
            assertThat(api.getReport(key).get(), equalTo(globalReport));
        });
    }

    @Test
    void testGettingUnsuccessfulRegionalReport_thenReturnAnEmptyOptional() {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedEmptyJson);
        when(httpClient.getHttpResponse("https://covid-193.p.rapidapi.com/history?country=Portugal&day=2022-04-30")).thenReturn(mockHttpResponse);

        Key key = Key.getRegionalKey("Portugal", LocalDate.parse("2022-04-30", formatter));

        assertTrue(api.getReport(key).isEmpty());
    }

    @Test
    void testGettingUnsuccessfulGlobalReport_thenReturnAnEmptyOptional() {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedEmptyJson);
        when(httpClient.getHttpResponse("https://covid-193.p.rapidapi.com/history?country=All&day=2022-04-30")).thenReturn(mockHttpResponse);

        Key key = Key.getGlobalKey(LocalDate.parse("2022-04-30", formatter));

        assertTrue(api.getReport(key).isEmpty());

        verify(httpClient, times(1)).getHttpResponse(any());
    }


    @Test
    void testGettingRegionsReturnsOK_thenReturnListOfRegions() {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedRegionsJsonNode);
        when(httpClient.getHttpResponse("https://covid-193.p.rapidapi.com/countries")).thenReturn(mockHttpResponse);

        assertThat(api.getAllRegions(), equalTo(Arrays.asList("Afghanistan", "Albania", "Algeria", "Andorra", "Puerto Rico")));
        verify(httpClient, times(1)).getHttpResponse(any());
    }

    @Test
    void testGettingRegionsReturnsNotOK_thenReturnEmptyList() {
        when(mockHttpResponse.getStatus()).thenReturn(400);

        when(httpClient.getHttpResponse("https://covid-193.p.rapidapi.com/countries")).thenReturn(mockHttpResponse);

        assertThat(api.getAllRegions(), empty());
        verify(httpClient, times(1)).getHttpResponse(any());
    }
}
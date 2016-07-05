package org.iobis;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MbonService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger logger = Logger.getLogger(MbonService.class);

    public String eovs() {
        PGobject result = jdbcTemplate.queryForObject("select coalesce(array_to_json(array_agg(a)), '[]') from ( select eov.id, name, ( select row_to_json(n) from ( select id, name from goos.parent where parent.id = eov.parent_id ) n ) as parent, ( select array_to_json(array_agg(row_to_json(t))) from ( select question.id, question.name from goos.eov_question left join goos.question on question.id = eov_question.question_id where eov_question.eov_id = eov.id ) t ) as questions from goos.eov ) a; ", PGobject.class);
        return result.getValue();
    }

    public String networks() {
        PGobject result = jdbcTemplate.queryForObject("select coalesce(array_to_json(array_agg(t)), '[]') from (select id, name, funding, ongoing from goos.network) t", PGobject.class);
        return result.getValue();
    }

    public String parents() {
        PGobject result = jdbcTemplate.queryForObject("select coalesce(array_to_json(array_agg(t)), '[]') from (select id, name from goos.parent) t", PGobject.class);
        return result.getValue();
    }

    public String tools() {
        PGobject result = jdbcTemplate.queryForObject("select coalesce(array_to_json(array_agg(t)), '[]') from (select id, name from goos.tool) t", PGobject.class);
        return result.getValue();
    }

    public String activities() {
        PGobject result = jdbcTemplate.queryForObject("select coalesce(array_to_json(array_agg(a)), '[]') from ( select activity.id, ST_AsText(ST_Envelope(coverage)) as coverage, timescale, ( select row_to_json(n) from ( select id, name from goos.network where network.id = activity.network_id ) n ) as network, ( select row_to_json(n) from ( select id, name from goos.eov where eov.id = activity.eov_id ) n ) as eov, ( select array_to_json(array_agg(row_to_json(t))) from ( select tool.id, tool.name from goos.activity_tool left join goos.tool on tool.id = activity_tool.tool_id where activity_tool.activity_id = activity.id ) t ) as tools from goos.activity ) a; ", PGobject.class);
        return result.getValue();
    }

    public String questions() {
        PGobject result = jdbcTemplate.queryForObject("select coalesce(array_to_json(array_agg(t)), '[]') from (select id, name from goos.question) t", PGobject.class);
        return result.getValue();
    }

    public String geojson() {
        PGobject result = jdbcTemplate.queryForObject("select row_to_json(fc)  from ( select 'FeatureCollection' as type, array_to_json(array_agg(f)) as features from ( select 'Feature' as type, ST_AsGeoJSON(lg.coverage)::json As geometry, row_to_json( ( select l from (select id) as l  ) ) as properties from goos.activity As lg ) as f ) as fc;", PGobject.class);
        return result.getValue();
    }

    public String output(Integer questionid) {
        PGobject result = jdbcTemplate.queryForObject("select row_to_json(a) from ( select   id,  (   select array_to_json(array_agg(row_to_json(t)))   from (    select eov.id, eov.name    from goos.eov_question eq    left join goos.eov on eov.id = eq.eov_id    where eq.question_id = question.id   ) t  ) as eovs,  (   select array_to_json(array_agg(row_to_json(t)))   from (    select network.id, network.name, network.ongoing    from goos.eov_question eq    left join goos.eov on eov.id = eq.eov_id    left join goos.activity on activity.eov_id = eov.id    left join goos.network on activity.network_id = network.id    where eq.question_id = question.id    group by network.id, network.name, network.ongoing   ) t  ) as networks,  (   select array_to_json(array_agg(row_to_json(t)))   from (    select     activity.id,     ST_AsText(ST_Envelope(activity.coverage)) as coverage,     network.name as network    from goos.eov_question eq    left join goos.eov on eov.id = eq.eov_id    left join goos.activity on activity.eov_id = eov.id    left join goos.network on network.id = activity.network_id    where eq.question_id = question.id    group by activity.id, activity.coverage, network.name   ) t  ) as activities,  (   select array_to_json(array_agg(row_to_json(t)))   from (    select tool.id, tool.name    from goos.eov_question eq    left join goos.eov on eov.id = eq.eov_id    left join goos.activity on activity.eov_id = eov.id    left join goos.activity_tool at on at.activity_id = activity.id    left join goos.tool on tool.id = at.tool_id    where eq.question_id = question.id    group by tool.id, tool.name   ) t  ) as tools from goos.question where question.id = ? ) a;", PGobject.class, questionid);
        return result.getValue();
    }

    public String graph() {
        PGobject result = jdbcTemplate.queryForObject("select array_to_json(array_agg(t)) from ( select array_agg(a) from ( select  question.name as question,  eov.name as eov,  activity.id as activity,  network.name as network,  tool.name as tool  from goos.question left join goos.eov_question eq on eq.question_id = question.id left join goos.eov on eov.id = eq.eov_id left join goos.activity on activity.eov_id = eov.id left join goos.network on network.id = activity.network_id left join goos.activity_tool at on at.activity_id = activity.id left join goos.tool on at.tool_id = tooL.id ) a ) t;", PGobject.class);
        return result.getValue();
    }

    public void saveNetwork(JSONObject network) {
        String name = network.has("name") ? network.getString("name") : null;
        String funding = network.has("funding") ? network.getString("funding"): null;
        Boolean ongoing = network.has("ongoing") ? network.getBoolean("ongoing") : false;

        jdbcTemplate.update("insert into network (name, funding, ongoing) values (?, ?, ?)", name, funding, ongoing);
    }

    public void saveQuestion(JSONObject question) {
        String name = question.has("name") ? question.getString("name") : null;

        jdbcTemplate.update("insert into question (name) values (?)", name);
    }

    public void saveEov(JSONObject eov) {

        // save eov

        String name = eov.has("name") ? eov.getString("name") : null;
        Integer parentid = null;
        if (eov.has("parent")) {
            JSONObject parent = eov.getJSONObject("parent");
            if (parent != null && parent.has("id")) {
                parentid = parent.getInt("id");
            }
        }

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withSchemaName("goos").withTableName("eov").usingGeneratedKeyColumns("id");;
        Map map = new HashMap();
        map.put("name", name);
        map.put("parent_id", parentid);
        Number id = insert.executeAndReturnKey(map);

        logger.info(id);

        // save activity_tool

        if (eov.has("questions")) {

            JSONArray questions = eov.getJSONArray("questions");

            for (int i = 0; i < questions.length(); i++) {
                JSONObject question = questions.getJSONObject(i);
                Integer questionid = question.has("id") ? question.getInt("id") : null;
                jdbcTemplate.update("insert into eov_question (eov_id, question_id) values (?, ?)", id, questionid);
            }

        }

    }

    public void saveActivity(JSONObject activity) {

        // save activity

        String timescale = activity.has("timescale") ? activity.getString("timescale") : null;
        Integer networkid =
                activity.has("network") &&
                        activity.getJSONObject("network").has("id") ?
                        activity.getJSONObject("network").getInt("id") : null;
        Integer eovid =
                activity.has("eov") &&
                        activity.getJSONObject("eov").has("id") ?
                        activity.getJSONObject("eov").getInt("id") : null;

        Geometry geom = null;
        if (activity.has("coverage")) {
            WKTReader reader = new WKTReader();
            try {
                geom = reader.read(activity.getString("coverage"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withSchemaName("goos").withTableName("activity").usingGeneratedKeyColumns("id");;
        Map map = new HashMap();
        map.put("timescale", timescale);
        map.put("network_id", networkid);
        map.put("eov_id", eovid);
        map.put("coverage", geom);
        Number id = insert.executeAndReturnKey(map);

        logger.info(id);

        // save activity_tool

        if (activity.has("tools")) {

            JSONArray tools = activity.getJSONArray("tools");

            for (int i = 0; i < tools.length(); i++) {
                JSONObject tool = tools.getJSONObject(i);
                Integer toolid = tool.has("id") ? tool.getInt("id") : null;
                jdbcTemplate.update("insert into activity_tool (activity_id, tool_id) values (?, ?)", id, toolid);
            }

        }

    }

    public void deleteNetwork(Integer id) {
        jdbcTemplate.update("delete from network where id = ?", id);
    }

    public void deleteActivity(Integer id) {
        jdbcTemplate.update("delete from activity where id = ?", id);
    }

    public void deleteQuestion(Integer id) {
        jdbcTemplate.update("delete from question where id = ?", id);
    }

    public void deleteEov(Integer id) {
        jdbcTemplate.update("delete from eov where id = ?", id);
    }

}

package org.heroesunlimited.com;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.heroesunlimited.com.config.CustomHandlebarsTemplateEngine;
import org.heroesunlimited.com.config.DbConfig;
import org.heroesunlimited.com.dao.HerosDao;
import org.heroesunlimited.com.dao.PowersDao;
import org.heroesunlimited.com.dao.SquadDao;
import org.heroesunlimited.com.dao.WeaknessesDao;
import org.heroesunlimited.com.dao.impl.Sql2oHeroDao;
import org.heroesunlimited.com.dao.impl.Sql2oPowerDao;
import org.heroesunlimited.com.dao.impl.Sql2oSquadDao;
import org.heroesunlimited.com.dao.impl.Sql2oWeaknessDao;
import org.heroesunlimited.com.exceptions.DataNotFoundException;
import org.heroesunlimited.com.models.Hero;
import org.heroesunlimited.com.models.Power;
import org.heroesunlimited.com.models.Squad;
import org.heroesunlimited.com.models.Weakness;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

    public static void main(String... args) {

        CustomHandlebarsTemplateEngine templateEngine = new CustomHandlebarsTemplateEngine();

        staticFileLocation("/public");

        // Initialize DAOs
        HerosDao herosDao = new Sql2oHeroDao(DbConfig.datasource());
        SquadDao squadDao = new Sql2oSquadDao(DbConfig.datasource());
        PowersDao powersDao = new Sql2oPowerDao(DbConfig.datasource());
        WeaknessesDao weaknessesDao = new Sql2oWeaknessDao(DbConfig.datasource());


        // show home page
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            //count of total heroes
            List<Hero> heros = herosDao.getAll();
            List<Hero> herosnotinsquad = herosDao.getAllNotInSquad();
            List<Squad> squads = squadDao.getAll();

            model.put("heroesWithoutSquads", herosnotinsquad);
            model.put("squads", squads);
            model.put("totalHeros", heros.size());

            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "index.hbs"));
        });

        // show hero list
        get("/heroes", (req, res) -> {
            List<Hero> heroes = herosDao.getAll();
            Map<String, Object> model = new HashMap<>();
            model.put("heroes", heroes);
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "heroes.hbs"));
        });

        // get add hero form
        get("/heroes/new", (req, res) -> {

            Map<String, Object> model = new HashMap<>();
            try {

                List<Power> powers = powersDao.getAll();
                List<Weakness> weaknesses = weaknessesDao.getAll();
                List<Squad> squads = squadDao.getAll();

                if (powers.isEmpty()) {
                    throw new DataNotFoundException("No Powers Found! Please create one!");
                }

                if (weaknesses.isEmpty()) {
                    throw new DataNotFoundException("No Weaknesses Found! Please create one!");
                }
                model.put("powers", powers);
                model.put("weaknesses", weaknesses);

                if (!squads.isEmpty()) {
                    model.put("squads", squads);
                }

            } catch (DataNotFoundException ex) {
                model.put("errorMessage", ex.getMessage());
            }

            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "heroForm.hbs"));

        });

        // add hero hero
        post("/heroes/new", (req, res) -> {
            //check if powers and weaknesses are present

            String heroname = req.queryParams("name");
            Integer age = Integer.valueOf(req.queryParams("age"));
            String weaknessname = req.queryParams("weakness");
            String powername = req.queryParams("power");
            String squadname = req.queryParams("squad");
            Squad squad = null;
            // find entities
            Power power = powersDao.findByName(powername);
            Weakness weakness = weaknessesDao.findByName(weaknessname);
            if (squadname != null) {
                squad = squadDao.findByname(squadname);
            }

            //create Hero object and save

            Hero newHero = new Hero(heroname, age, power.getId(), weakness.getId());

            if (squad != null) {
                newHero.setSquadId(squad.getId());
            }

            //save new hero

            herosDao.add(newHero);

            res.redirect("/heroes");


            return null;
        });

        // get update hero form
        get("/heroes/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Integer heroId = Integer.parseInt(req.params("id"));
            Hero hero = herosDao.findById(heroId);
            model.put("editHero",hero);

            List<Power> powers = powersDao.getAll();
            List<Weakness> weaknesses = weaknessesDao.getAll();
            List<Squad> squads = squadDao.getAll();

            if (powers.isEmpty()) {
                throw new DataNotFoundException("No Powers Found! Please create one!");
            }

            if (weaknesses.isEmpty()) {
                throw new DataNotFoundException("No Weaknesses Found! Please create one!");
            }
            model.put("powers", powers);
            model.put("weaknesses", weaknesses);

            if (!squads.isEmpty()) {
                model.put("squads", squads);
            }


            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "heroForm.hbs"));
        });

        // update  hero
        post("/heroes/:id", (req, res) -> {
            // Code to handle hero form submission
            Hero updatehero = herosDao.findById(Integer.parseInt(req.params("id")));
            if(req.queryParams("age") != null)
            {
             updatehero.setAge(Integer.parseInt(req.queryParams("age")));
            }

            if(req.queryParams("power") != null)
            {
                updatehero.setPowerId(powersDao.findByName(req.queryParams("power")).getId());
            }

            if(req.queryParams("weakness") != null)
            {
                updatehero.setWeaknessId(weaknessesDao.findByName(req.queryParams("weakness")).getId());
            }

            if(req.queryParams("squad") != null)
            {
                updatehero.setSquadId(squadDao.findByName(req.queryParams("squad")).getId());
            }

            herosDao.update(updatehero.getId(),updatehero.getSquadId(),updatehero.getPowerId(),updatehero.getWeaknessId());

            res.redirect("/heroes");
            return null;
        });


        // delete a hero
        get("/heroes/:id/delete", (req, res) -> {
            int heroId = Integer.parseInt(req.params("id"));
            herosDao.deleteById(heroId);
            res.redirect("/heroes");
            return null;
        });

        // show squad list
        get("/squads", (req, res) -> {
            List<Squad> squads = squadDao.getAll();
            Map<String, Object> model = new HashMap<>();
            model.put("squads", squads);
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "squads.hbs"));
        });

        // show add squad form
        get("/squads/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "squadForm.hbs"));
        });
        // add squad
        post("/squads/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            String cause = req.queryParams("cause");
            squadDao.add(new Squad(name, cause));
            res.redirect("/squads");
            return null;
        });

        get("/squads/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            // Code to retrieve necessary data for the form
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "squadForm.hbs"));
        });

        // update squad
        post("/squads/:id", (req, res) -> {
            // Code to handle squad form submission
            res.redirect("/squads");
            return null;
        });

        // delete a squad
        get("/squads/:id/delete", (req, res) -> {
            int squadId = Integer.parseInt(req.params("id"));
            squadDao.deleteById(squadId);
            res.redirect("/squads");
            return null;
        });

        // show powers list
        get("/powers", (req, res) -> {
            List<Power> powers = powersDao.getAll();
            Map<String, Object> model = new HashMap<>();
            model.put("powers", powers);
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "powers.hbs"));
        });

        // show new power form
        get("/powers/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            // Code to retrieve necessary data for the form
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "powerForm.hbs"));
        });

        // add  a power
        post("/powers/new", (req, res) -> {

            String name = req.queryParams("name");
            String description = req.queryParams("description");

            powersDao.add(new Power(name, description));
            res.redirect("/powers");
            return null;
        });

        // show update power form
        get("/powers/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            // Code to retrieve necessary data for the form
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "powerForm.hbs"));
        });

        // update a power
        post("/powers/:id", (req, res) -> {
            // Code to handle power form submission
            res.redirect("/powers");
            return null;
        });

        // delete a power
        get("/powers/:id/delete", (req, res) -> {
            int powerId = Integer.parseInt(req.params("id"));
            powersDao.deleteById(powerId);
            res.redirect("/powers");
            return null;
        });

        // show weaknesses list
        get("/weaknesses", (req, res) -> {
            List<Weakness> weaknesses = weaknessesDao.getAll();
            Map<String, Object> model = new HashMap<>();
            model.put("weaknesses", weaknesses);
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "weaknesses.hbs"));
        });

        // show add weakness form
        get("/weaknesses/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            // Code to retrieve necessary data for the form
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "weaknessForm.hbs"));
        });

        // add a weakness
        post("/weaknesses/new", (req, res) -> {
            String name = req.queryParams("name");
            String description = req.queryParams("description");

            weaknessesDao.add(new Weakness(name, description));
            res.redirect("/weaknesses");
            return null;
        });

        // show update weakness form
        get("/weaknesses/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            // Code to retrieve necessary data for the form
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "weaknessForm.hbs"));
        });

        // update a weakness
        post("/weaknesses/:id", (req, res) -> {
            // Code to handle weakness form submission
            res.redirect("/weaknesses");
            return null;
        });

        // delete a power
        get("/weaknesses/:id/delete", (req, res) -> {
            int weaknessId = Integer.parseInt(req.params("id"));
            weaknessesDao.deleteById(weaknessId);
            res.redirect("/weaknesses");
            return null;
        });


    }
}

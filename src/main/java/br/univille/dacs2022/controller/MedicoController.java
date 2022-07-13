package br.univille.dacs2022.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import br.univille.dacs2022.dto.MedicoDTO;
import br.univille.dacs2022.service.MedicoService;
import br.univille.dacs2022.service.ProcedimentoService;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;
    @Autowired
    private ProcedimentoService procedimentoService; 

    @GetMapping
    public ModelAndView index(){
        List<MedicoDTO> listaMedicos = medicoService.getAll();
        return new ModelAndView("medico/index", "listaMedicos", listaMedicos);
    }

    @GetMapping("/novo")
    public ModelAndView novo() {
        var medico = new MedicoDTO();
        var listaProcedimentos = procedimentoService.getAll();
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("medico", medico);
        dados.put("listaProcedimentos", listaProcedimentos);
        return new ModelAndView("medico/form", dados);
    }

    @PostMapping(params="save")
    public ModelAndView save(@Valid @ModelAttribute("medico") MedicoDTO medico, BindingResult BindingResult) {
        if(BindingResult.hasErrors()){
            return new ModelAndView("medico/form");
        }
        medicoService.save(medico);
        return new ModelAndView("redirect:/medico");
    }

    @GetMapping(path = "/alterar/{id}")
    public ModelAndView alterar(@PathVariable("id") long id) {
        MedicoDTO medico = medicoService.findById(id);
        var listaProcedimentos = procedimentoService.getAll();
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("medico", medico);
        dados.put("listaProcedimentos", listaProcedimentos);
        return new ModelAndView("medico/form", dados);
    }

    @GetMapping(path = "/delete/{id}")
    public ModelAndView delete(@PathVariable("id") long id) {
        medicoService.delete(id);
        return new ModelAndView("redirect:/medico");
    }

    @PostMapping(params="incProc")
    public ModelAndView incluirProcedimento(@ModelAttribute("medico") MedicoDTO medico, BindingResult bindingResult) {
        long idProcedimentoSelect = medico.getProcedimentoId();
        var procedimentoSelect = procedimentoService.findById(idProcedimentoSelect);

        if (!medico.getListaProcedimentos().contains(procedimentoSelect)) {
            medico.getListaProcedimentos().add(procedimentoSelect);
        }

        var listaProcedimentos = procedimentoService.getAll();
        HashMap<String, Object> dados = new HashMap<>();

        dados.put("medico", medico);
        dados.put("listaProcedimentos", listaProcedimentos);

        return new ModelAndView("medico/form", dados);
    }

    @PostMapping(params="removeProc")
    public ModelAndView removerProcedimento(@ModelAttribute("medico") MedicoDTO medico, @RequestParam(name = "removeProc") int index, BindingResult bindingResult) {
        medico.getListaProcedimentos().remove(index);

        var listaProcedimentos = procedimentoService.getAll();
        HashMap<String, Object> dados = new HashMap<>();

        dados.put("medico", medico);
        dados.put("listaProcedimentos", listaProcedimentos);

        return new ModelAndView("medico/form", dados);
    }
}

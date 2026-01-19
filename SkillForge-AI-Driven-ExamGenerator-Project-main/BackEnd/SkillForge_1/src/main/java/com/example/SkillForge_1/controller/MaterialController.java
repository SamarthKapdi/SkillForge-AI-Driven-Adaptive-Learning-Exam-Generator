//package com.example.SkillForge_1.controller;
//
//import com.example.SkillForge_1.model.Material;
//import com.example.SkillForge_1.model.MaterialType;
//import com.example.SkillForge_1.service.MaterialService;
//import com.example.SkillForge_1.service.MaterialServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/materials")
//@CrossOrigin
//public class MaterialController {
//
//    private final MaterialService materialService;
//
//    public MaterialController(MaterialService materialService) {
//        this.materialService = materialService;
//    }
//
//    // Add material: send JSON { title, materialType, contentUrl }
//
//    // Get all materials
//    @GetMapping
//    public List<Material> getMaterials() {
//        return materialService.getAllMaterials();
//    }
//
//    // Get single material
//    @GetMapping("/{id}")
//    public Material getMaterial(@PathVariable Long id) {
//        return materialService.getMaterialById(id);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteMaterial(@PathVariable Long id) {
//        materialService.deleteMaterial(id);
//    }
//    @GetMapping("/topic/{topicId}")
//    public List<Material> getMaterialsByTopic(@PathVariable Long topicId) {
//        return materialService.getMaterialsByTopic(topicId);
//    }
//
//
//}
package com.example.SkillForge_1.controller;

import com.example.SkillForge_1.model.Material;
import com.example.SkillForge_1.service.MaterialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@CrossOrigin
public class MaterialController {


    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    // Add material to a topic
    @PostMapping("/topic/{topicId}")
    public Material addMaterial(
            @PathVariable Long topicId,
            @RequestBody Material material) {

        if (material.getMaterialType() == null) {
            throw new IllegalArgumentException("materialType is required");
        }

        return materialService.addMaterial(material, topicId);
    }

    // Get materials by topic
    @GetMapping("/topic/{topicId}")
    public List<Material> getMaterialsByTopic(@PathVariable Long topicId) {
        return materialService.getMaterialsByTopic(topicId);
    }

    // Delete material
    @DeleteMapping("/{id}")
    public void deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
    }
}

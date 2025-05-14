-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema iweb_proy
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema iweb_proy
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `iweb_proy` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `iweb_proy` ;

-- -----------------------------------------------------
-- Table `iweb_proy`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`categoria` (
  `idcategoria` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idcategoria`))
ENGINE = InnoDB
AUTO_INCREMENT = 105
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`zona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`zona` (
  `idzona` INT NOT NULL,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idzona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`distritos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`distritos` (
  `iddistritos` INT NOT NULL,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  `zona_idzona` INT NOT NULL,
  PRIMARY KEY (`iddistritos`, `zona_idzona`),
  INDEX `fk_distritos_zona1_idx` (`zona_idzona` ASC) VISIBLE,
  CONSTRAINT `fk_distritos_zona1`
    FOREIGN KEY (`zona_idzona`)
    REFERENCES `iweb_proy`.`zona` (`idzona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`formulario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`formulario` (
  `idformulario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(80) NULL,
  `fecha_creacion` DATE NOT NULL,
  `fecha_limite` DATE NOT NULL,
  `estado` TINYINT NOT NULL DEFAULT '1',
  `registros_esperados` INT NULL,
  `categoria_idcategoria` INT NOT NULL,
  PRIMARY KEY (`idformulario`),
  INDEX `fk_formulario_categoria1_idx` (`categoria_idcategoria` ASC) VISIBLE,
  CONSTRAINT `fk_formulario_categoria1`
    FOREIGN KEY (`categoria_idcategoria`)
    REFERENCES `iweb_proy`.`categoria` (`idcategoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`seccion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`seccion` (
  `idseccion` INT NOT NULL AUTO_INCREMENT,
  `nombre_sec` VARCHAR(40) NOT NULL,
  `formulario_idformulario` INT NOT NULL,
  PRIMARY KEY (`idseccion`),
  INDEX `fk_seccion_formulario1_idx` (`formulario_idformulario` ASC) VISIBLE,
  CONSTRAINT `fk_seccion_formulario1`
    FOREIGN KEY (`formulario_idformulario`)
    REFERENCES `iweb_proy`.`formulario` (`idformulario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iweb_proy`.`pregunta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`pregunta` (
  `idpregunta` INT NOT NULL AUTO_INCREMENT,
  `enunciado` VARCHAR(400) NULL DEFAULT NULL,
  `tipo_dato` VARCHAR(40) NULL,
  `seccion_idseccion` INT NOT NULL,
  PRIMARY KEY (`idpregunta`),
  INDEX `fk_preguntas_seccion1_idx` (`seccion_idseccion` ASC) VISIBLE,
  CONSTRAINT `fk_preguntas_seccion1`
    FOREIGN KEY (`seccion_idseccion`)
    REFERENCES `iweb_proy`.`seccion` (`idseccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`roles` (
  `idroles` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idroles`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`usuario` (
  `idusuario` INT NOT NULL AUTO_INCREMENT,
  `nombres` VARCHAR(150) NULL DEFAULT NULL,
  `apellidos` VARCHAR(150) NULL DEFAULT NULL,
  `contraseña` VARCHAR(255) NOT NULL,
  `DNI` INT(8) NOT NULL,
  `correo` VARCHAR(150) NOT NULL,
  `direccion` VARCHAR(200) NULL DEFAULT NULL,
  `estado` TINYINT NOT NULL,
  `roles_idroles` INT NOT NULL,
  `zona_idzona` INT NULL DEFAULT NULL,
  `distritos_iddistritos` INT NULL DEFAULT NULL,
  `fecha_incorporacion` DATE NOT NULL,
  `foto` BLOB NULL DEFAULT NULL,
  `cod_enc` VARCHAR(30) NULL,
  PRIMARY KEY (`idusuario`),
  UNIQUE INDEX `DNI_UNIQUE` (`DNI` ASC) VISIBLE,
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC) VISIBLE,
  INDEX `fk_usuario_roles1_idx` (`roles_idroles` ASC) VISIBLE,
  INDEX `fk_usuario_zona1_idx` (`zona_idzona` ASC) VISIBLE,
  INDEX `fk_usuario_distritos1_idx` (`distritos_iddistritos` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_distritos1`
    FOREIGN KEY (`distritos_iddistritos`)
    REFERENCES `iweb_proy`.`distritos` (`iddistritos`),
  CONSTRAINT `fk_usuario_roles1`
    FOREIGN KEY (`roles_idroles`)
    REFERENCES `iweb_proy`.`roles` (`idroles`),
  CONSTRAINT `fk_usuario_zona1`
    FOREIGN KEY (`zona_idzona`)
    REFERENCES `iweb_proy`.`zona` (`idzona`))
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`enc_has_formulario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`enc_has_formulario` (
  `idenc_has_formulario` INT NOT NULL AUTO_INCREMENT,
  `enc_idusuario` INT NOT NULL,
  `formulario_idformulario` INT NOT NULL,
  `codigo` VARCHAR(45) NOT NULL,
  `fecha_asignacion` DATE NOT NULL,
  INDEX `fk_usuario_has_formulario_formulario1_idx` (`formulario_idformulario` ASC) VISIBLE,
  INDEX `fk_usuario_has_formulario_usuario1_idx` (`enc_idusuario` ASC) VISIBLE,
  UNIQUE INDEX `codigo_UNIQUE` (`codigo` ASC) VISIBLE,
  PRIMARY KEY (`idenc_has_formulario`),
  CONSTRAINT `fk_usuario_has_formulario_formulario1`
    FOREIGN KEY (`formulario_idformulario`)
    REFERENCES `iweb_proy`.`formulario` (`idformulario`),
  CONSTRAINT `fk_usuario_has_formulario_usuario1`
    FOREIGN KEY (`enc_idusuario`)
    REFERENCES `iweb_proy`.`usuario` (`idusuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`registro_respuestas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`registro_respuestas` (
  `idregistro_respuestas` INT NOT NULL AUTO_INCREMENT,
  `fecha_registro` DATE NOT NULL,
  `estado` CHAR(1) NOT NULL,
  `enc_has_formulario_idenc_has_formulario` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idregistro_respuestas`),
  INDEX `fk_registro_respuesta_enc_has_formulario1_idx` (`enc_has_formulario_idenc_has_formulario` ASC) VISIBLE,
  CONSTRAINT `fk_registro_respuesta_enc_has_formulario1`
    FOREIGN KEY (`enc_has_formulario_idenc_has_formulario`)
    REFERENCES `iweb_proy`.`enc_has_formulario` (`idenc_has_formulario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iweb_proy`.`respuesta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`respuesta` (
  `idrespuesta` INT NOT NULL AUTO_INCREMENT,
  `respuesta` VARCHAR(400) NULL DEFAULT NULL,
  `pregunta_idregunta` INT NOT NULL,
  `reg_respuestas_idregistro_respuestas` INT NOT NULL,
  PRIMARY KEY (`idrespuesta`),
  INDEX `fk_respuesta_Preguntas1_idx` (`pregunta_idregunta` ASC) VISIBLE,
  INDEX `fk_respuesta_registro_respuesta1_idx` (`reg_respuestas_idregistro_respuestas` ASC) VISIBLE,
  CONSTRAINT `fk_respuesta_Preguntas1`
    FOREIGN KEY (`pregunta_idregunta`)
    REFERENCES `iweb_proy`.`pregunta` (`idpregunta`),
  CONSTRAINT `fk_respuesta_registro_respuesta1`
    FOREIGN KEY (`reg_respuestas_idregistro_respuestas`)
    REFERENCES `iweb_proy`.`registro_respuestas` (`idregistro_respuestas`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`opciones_pregunta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`opciones_pregunta` (
  `idopciones_pregunta` INT NOT NULL,
  `opciones` JSON NULL,
  `pregunta_idpregunta` INT NOT NULL,
  PRIMARY KEY (`idopciones_pregunta`),
  INDEX `fk_opciones_pregunta_pregunta1_idx` (`pregunta_idpregunta` ASC) VISIBLE,
  CONSTRAINT `fk_opciones_pregunta_pregunta1`
    FOREIGN KEY (`pregunta_idpregunta`)
    REFERENCES `iweb_proy`.`pregunta` (`idpregunta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `iweb_proy`.`pregunta_demo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`pregunta_demo` (
  `idpregunta` INT NOT NULL AUTO_INCREMENT,
  `enunciado` VARCHAR(400) NULL DEFAULT NULL,
  `seccion_idseccion` INT NOT NULL,
  PRIMARY KEY (`idpregunta`),
  INDEX `fk_preguntas_seccion1_idx` (`seccion_idseccion` ASC) VISIBLE,
  CONSTRAINT `fk_preguntas_seccion10`
    FOREIGN KEY (`seccion_idseccion`)
    REFERENCES `iweb_proy`.`seccion` (`idseccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`respuesta_demo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`respuesta_demo` (
  `idrespuesta` INT NOT NULL AUTO_INCREMENT,
  `respuesta` VARCHAR(400) NULL DEFAULT NULL,
  `reg_respuestas_idregistro_respuestas` INT NOT NULL,
  `pregunta_copy1_idpregunta` INT NOT NULL,
  PRIMARY KEY (`idrespuesta`),
  INDEX `fk_respuesta_registro_respuesta1_idx` (`reg_respuestas_idregistro_respuestas` ASC) VISIBLE,
  INDEX `fk_respuesta_demo_pregunta_copy11_idx` (`pregunta_copy1_idpregunta` ASC) VISIBLE,
  CONSTRAINT `fk_respuesta_registro_respuesta10`
    FOREIGN KEY (`reg_respuestas_idregistro_respuestas`)
    REFERENCES `iweb_proy`.`registro_respuestas` (`idregistro_respuestas`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_respuesta_demo_pregunta_copy11`
    FOREIGN KEY (`pregunta_copy1_idpregunta`)
    REFERENCES `iweb_proy`.`pregunta_demo` (`idpregunta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

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
  `idzona` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idzona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`distritos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`distritos` (
  `iddistritos` INT NOT NULL AUTO_INCREMENT,
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
  `nombre` VARCHAR(100) NOT NULL, -- Título o nombre descriptivo del formulario
  `fecha_creacion` DATE NOT NULL,
  `fecha_limite` DATE NOT NULL,
  `estado` TINYINT NOT NULL DEFAULT '1',
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
-- Table `iweb_proy`.`preguntas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`preguntas` (
  `idPreguntas` INT NOT NULL AUTO_INCREMENT,
  `enunciado` VARCHAR(400) NULL DEFAULT NULL,
  `formulario_idformulario` INT NOT NULL,
  PRIMARY KEY (`idPreguntas`),
  INDEX `fk_Preguntas_formulario1_idx` (`formulario_idformulario` ASC) VISIBLE,
  CONSTRAINT `fk_Preguntas_formulario1`
    FOREIGN KEY (`formulario_idformulario`)
    REFERENCES `iweb_proy`.`formulario` (`idformulario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`respuesta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`respuesta` (
  `idrespuesta` INT NOT NULL AUTO_INCREMENT,
  `Preguntas_idPreguntas` INT NOT NULL,
  `respuesta` VARCHAR(400) NULL DEFAULT NULL,
  PRIMARY KEY (`idrespuesta`),
  INDEX `fk_respuesta_Preguntas1_idx` (`Preguntas_idPreguntas` ASC) VISIBLE,
  CONSTRAINT `fk_respuesta_Preguntas1`
    FOREIGN KEY (`Preguntas_idPreguntas`)
    REFERENCES `iweb_proy`.`preguntas` (`idPreguntas`))
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
  `idusuario` INT NOT NULL,
  `nombres` VARCHAR(150) NULL DEFAULT NULL,
  `apellidos` VARCHAR(150) NULL DEFAULT NULL,
  `contraseña` VARCHAR(45) NOT NULL,
  `DNI` INT NULL DEFAULT NULL,
  `correo` VARCHAR(150) NOT NULL,
  `direccion` VARCHAR(200) NULL DEFAULT NULL,
  `estado` TINYINT NOT NULL,
  `roles_idroles` INT NOT NULL,
  `zona_idzona` INT NULL DEFAULT NULL,
  `distritos_iddistritos` INT NULL DEFAULT NULL,
  `fecha_incorporacion` DATE NOT NULL,
  `foto` BLOB NULL DEFAULT NULL,
  PRIMARY KEY (`idusuario`),
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
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `iweb_proy`.`usuario_has_formulario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `iweb_proy`.`usuario_has_formulario` (
  `usuario_idusuario` INT NOT NULL,
  `formulario_idformulario` INT NOT NULL,
  `respuesta_idrespuesta` INT NOT NULL,
  `codigo` VARCHAR(45) NOT NULL,
  `asignado_por` INT NOT NULL, -- ID del coordinador que asignó el formulario
  `fecha_asignacion` DATE NOT NULL,
  `estado_rpta` CHAR(1) NOT NULL,
  `fecha_registro` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`usuario_idusuario`, `formulario_idformulario`),
  INDEX `fk_usuario_has_formulario_formulario1_idx` (`formulario_idformulario` ASC) VISIBLE,
  INDEX `fk_usuario_has_formulario_usuario1_idx` (`usuario_idusuario` ASC) VISIBLE,
  INDEX `fk_usuario_has_formulario_respuesta1_idx` (`respuesta_idrespuesta` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_has_formulario_formulario1`
    FOREIGN KEY (`formulario_idformulario`)
    REFERENCES `iweb_proy`.`formulario` (`idformulario`),
  CONSTRAINT `fk_usuario_has_formulario_respuesta1`
    FOREIGN KEY (`respuesta_idrespuesta`)
    REFERENCES `iweb_proy`.`respuesta` (`idrespuesta`),
  CONSTRAINT `fk_usuario_has_formulario_usuario1`
    FOREIGN KEY (`usuario_idusuario`)
    REFERENCES `iweb_proy`.`usuario` (`idusuario`)),
  CONSTRAINT `fk_usuario_has_formulario_asignado_por`
	FOREIGN KEY (`asignado_por`)
    REFERENCES `usuario` (`idusuario`)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

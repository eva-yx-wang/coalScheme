/*
 Navicat Premium Data Transfer

 Source Server         : mysql_192.168.1.5
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : coalscheme_test1

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 21/06/2021 14:20:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for coal_analysis
-- ----------------------------
DROP TABLE IF EXISTS `coal_analysis`;
CREATE TABLE `coal_analysis`  (
  `coal_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '煤种编号',
  `coal_m_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基全水分',
  `coal_m_ad` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '空干基水分',
  `coal_a_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基灰分',
  `coal_v_daf` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '可燃基挥发分',
  `coal_v_ad` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '空干基挥发分',
  `coal_qnet` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '低位发热量',
  `coal_c_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基碳',
  `coal_h_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基氢',
  `coal_n_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基氮',
  `coal_s_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基全硫',
  `coal_o_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基氧',
  `ash_dt_ar` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '灰变形温度',
  `ash_st_ar` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '灰软化温度',
  `ash_ft_ar` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '灰流动温度',
  `ash_sio2_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '二氧化硅',
  `ash_al2o3_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '三氧化二铝',
  `ash_fe2o3_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '三氧化二铁',
  `ash_tio2` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '二氧化钛',
  `ash_cao` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '氧化钙',
  `ash_mgo` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '氧化镁',
  `ash_so3` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '三氧化硫',
  `ash_k2o` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '氧化钾',
  `ash_na20` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '氧化钠',
  PRIMARY KEY (`coal_id`) USING BTREE,
  CONSTRAINT `fk_coal_inplo` FOREIGN KEY (`coal_id`) REFERENCES `coal_inplo` (`coal_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '煤质管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coal_inplo
-- ----------------------------
DROP TABLE IF EXISTS `coal_inplo`;
CREATE TABLE `coal_inplo`  (
  `coal_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '煤种编号',
  `coal_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '煤种名称',
  `coal_price` decimal(5, 0) NULL DEFAULT 0 COMMENT '煤种价格(元/t)',
  `coal_load` decimal(8, 0) NULL DEFAULT 0 COMMENT '煤种储量(t)',
  `coal_origin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'ABC' COMMENT '煤种产地',
  PRIMARY KEY (`coal_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '煤种查询' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coal_scheme
-- ----------------------------
DROP TABLE IF EXISTS `coal_scheme`;
CREATE TABLE `coal_scheme`  (
  `scheme_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '方案编号',
  `mixed_id` int(11) NOT NULL COMMENT '混煤编号',
  `min_v_daf` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '最小可燃基挥发分(%)',
  `max_m_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '最大收到基水分(%)',
  `max_a_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '最大收到基灰分(%)',
  `max_s_ad` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '最大空干基硫分(%)',
  `min_qnet_ar` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '最小收到基低位发热量(kcal)',
  `min_soft_t` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '最小软化温度(°C)',
  `sox` decimal(5, 1) NULL DEFAULT 0.0 COMMENT 'SOx估计(mg/Nm3)',
  `nox` decimal(5, 1) NULL DEFAULT 0.0 COMMENT 'NOx估计(mg/Nm3)',
  `t_env` decimal(4, 2) NULL DEFAULT 20.00 COMMENT '环境温度(°C)',
  `boiler_eff` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '锅炉效率(%)',
  `mixed_consump_kwh` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '混煤煤耗(g/kwh)',
  `stand_consump_kwh` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '标煤煤耗(g/kwh)',
  `boilder_load` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '机组负荷(MW)',
  `steam_flow` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '主汽流量(t/h)',
  `mixed_price` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '混煤单价(元/t)',
  `lime_price` decimal(3, 1) NULL DEFAULT 0.0 COMMENT '石灰单价(元/t)',
  `nh3_price` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '液氨单价(元/t)',
  `power_price` decimal(5, 4) NULL DEFAULT 0.0000 COMMENT '上网电价(元/kwh)',
  `lime_consump` decimal(3, 1) NULL DEFAULT 0.0 COMMENT '石灰耗量(t/天)',
  `lime_cost` decimal(5, 1) NULL DEFAULT 0.0 COMMENT '石灰成本(元/天)',
  `nh3_consump` decimal(3, 2) NULL DEFAULT 0.00 COMMENT '液氨耗量(t/天)',
  `nh3_cost` decimal(5, 1) NULL DEFAULT 0.0 COMMENT '脱硝成本(元/天)',
  `power_generate` decimal(4, 1) NULL DEFAULT 0.0 COMMENT '日发电量(万度/天)',
  `ratio_own` decimal(2, 1) NULL DEFAULT 0.0 COMMENT '厂用电率(%)',
  `power_supply` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '日供电量(万度/天)',
  `power_cost_kwh` decimal(5, 4) NULL DEFAULT 0.0000 COMMENT '供电成本(元/kwh)',
  `power_cost_day` decimal(7, 1) NULL DEFAULT 0.0 COMMENT '生产成本(元/天)',
  PRIMARY KEY (`scheme_id`) USING BTREE,
  INDEX `fk_mixed_id`(`mixed_id`) USING BTREE,
  CONSTRAINT `fk_mixed_id` FOREIGN KEY (`mixed_id`) REFERENCES `mixed_analysis` (`mixed_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '配煤方案管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mixed_analysis
-- ----------------------------
DROP TABLE IF EXISTS `mixed_analysis`;
CREATE TABLE `mixed_analysis`  (
  `mixed_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '方案编号',
  `mixed_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '方案名称',
  `coal1_id` int(11) NOT NULL COMMENT '煤种1编号',
  `coal2_id` int(11) NOT NULL COMMENT '煤种2编号',
  `coal1_ratio` decimal(4, 2) NULL DEFAULT 80.00 COMMENT '煤种1比率(%)',
  `mixed_c_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基碳(%)',
  `mixed_h_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基氢(%)',
  `mixed_o_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基氧(%)',
  `mixed_n_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基氮(%)',
  `mixed_s_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基全硫(%)',
  `mixed_s_ad` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '空干基硫(%)',
  `mixed_a_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基灰分(%)',
  `mixed_m_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '收到基全水分(%)',
  `mixed_m_ad` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '空干基水分(%)',
  `mixed_v_daf` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '可燃基挥发分(%)',
  `mixed_qnet_ar` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '收到基低位发热量(kcal)',
  `ash_sio2_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '二氧化硅(%)',
  `ash_al2o3_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '三氧化二铝(%)',
  `ash_fe2o3_ar` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '三氧化二铁(%)',
  `ash_tio2` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '二氧化钛(%)',
  `ash_cao` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '氧化钙(%)',
  `ash_mgo` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '氧化镁(%)',
  `ash_so3` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '三氧化硫(%)',
  `ash_k2o` decimal(4, 2) NULL DEFAULT 0.00 COMMENT '氧化钾(%)',
  `soft_t` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '软化温度(°C)',
  PRIMARY KEY (`mixed_id`) USING BTREE,
  UNIQUE INDEX `mixed_name`(`mixed_name`) USING BTREE,
  INDEX `fk_coal1_id`(`coal1_id`) USING BTREE,
  INDEX `fk_coal2_id`(`coal2_id`) USING BTREE,
  CONSTRAINT `fk_coal1_id` FOREIGN KEY (`coal1_id`) REFERENCES `coal_analysis` (`coal_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_coal2_id` FOREIGN KEY (`coal2_id`) REFERENCES `coal_analysis` (`coal_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '混煤煤质管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员编号',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `user_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `user_level` int(1) NULL DEFAULT 1 COMMENT '权限等级',
  `enabled` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户管理' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

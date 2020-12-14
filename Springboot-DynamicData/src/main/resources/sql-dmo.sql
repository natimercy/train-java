create
    definer = hw@`%` procedure p_map_garbage_front_stat_source_list(IN pi_type int, IN pi_id varchar(36),
                                                                    IN pi_source_type int, IN pi_begin_index int,
                                                                    IN pi_page_size int, IN pi_where varchar(200),
                                                                    IN pi_sort varchar(255))
BEGIN
    /*-------------------------------------------------------------------
    存储过程描述:地图监控-前端管控--源头明细
    -------------------------------------------------------------------*/
    DECLARE v_page_count INT;
    DECLARE v_record_count INT;

    SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
    DROP TEMPORARY TABLE IF EXISTS tmp_section_id;
    CREATE TEMPORARY TABLE tmp_section_id
    (
        c_section_id VARCHAR(36)
    );

    IF pi_type = 1
    THEN

        CALL p_get_viewable_sections(pi_id);

    ELSEIF pi_type = 2 THEN

        INSERT INTO tmp_section_id (c_section_id)
        VALUES (pi_id);

    END IF;


    DROP TEMPORARY TABLE IF EXISTS tmp_result1;
    CREATE TEMPORARY TABLE tmp_result1
    (
        c_source_type             int,                        #源头类型：1:小区;2:城中村,3:公园,4:集贸市场,5:单位,6:酒店,7:小门店,8:其他
        c_source_id               varchar(36),                #源头ID
        c_name                    varchar(200),               #名称
        c_position                VARCHAR(36),                #地址
        c_jmfs_count              int            default (0), #居民户数
        c_point_count             INT            DEFAULT (0), #投放点个数
        c_point_cover_person      decimal(18, 1) DEFAULT (0), #投放点平均覆盖户数
        c_point_cover_lv          DECIMAL(18, 2) DEFAULT (0), #投放点覆盖率
        c_supervisor_record_count int            DEFAULT (0), #督导记录
        c_geo_x                   DOUBLE,                     #经度
        c_geo_y                   DOUBLE,                     #纬度
        c_geomCol                 geometry,                   #经纬度多边形
        c_icon_type               INT            DEFAULT 0    #图标状态：预留
    );

    #调用分页过程1
    CALL p_common_page('tmp_result1',
                       'c_source_type  ,     /*源头类型：1:小区;2:城中村,3:公园,4:集贸市场,5:单位,6:酒店,7:小门店,8:其他*/
		        c_source_id    ,     /*源头ID*/
		        c_name         ,     /*名称*/
		        c_position     ,     /*地址*/
		        c_jmfs_count   ,     /*居民户数*/
		        c_point_count  ,     /*投放点个数*/
		        c_point_cover_person,/*投放点平均覆盖户数*/
		        c_point_cover_lv ,   /*投放点覆盖率*/
		        c_supervisor_record_count ,/*督导记录*/
		        c_geo_x        ,           /*经度*/
		        c_geo_y        ,           /*纬度*/
		        c_geomCol      ,           /*经纬度多边形*/
		        c_icon_type                /*图标状态：预留*/
	                            ',
                       pi_where,
                       pi_begin_index,
                       pi_page_size,
                       pi_sort,
                       v_page_count,
                       v_record_count);
    #返回结果集2
    SELECT v_page_count   AS c_page_count #总页数
         , v_record_count AS c_record_count; #总记录数

    DROP TEMPORARY TABLE IF EXISTS tmp_result2;
    CREATE TEMPORARY TABLE tmp_result2
    (
        c_refuse_classify_point_id varchar(36),  #投放点ID
        c_name                     VARCHAR(200), #投放点名称
        c_source_id                varchar(36),  #投放点
        c_position                 varchar(200), #位置
        c_geo_x                    DOUBLE,       #经度
        c_geo_y                    DOUBLE,       #纬度
        c_geomCol                  GEOMETRY      #经纬度多边形
    );


    #2、返回垃圾源头列表
    select c_refuse_classify_point_id, #投放点ID
           c_name,                     #投放点名称
           c_source_id,                #投放点
           c_position,                 #位置
           c_geo_x,                    #经度
           c_geo_y,                    #纬度
           c_geomCol                   #经纬度多边形
    from tmp_result2;


    SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;


END;


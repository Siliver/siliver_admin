INSERT INTO admin_schema.t_user (create_user, update_user, username, password)
VALUES ('admin', 'admin', 'admin', '$2a$10$ZvUjv.B5njT92KTtNhgBPu2cZflKz1mL8e.YJDhuj/YzF5qzWInO2');

INSERT INTO admin_schema.t_group (create_user, update_user, group_code, group_name, super_id)
VALUES ('admin', 'admin', 'admin', '超级管理员', 0);

INSERT INTO admin_schema.t_user_group (create_user, update_user, user_id, group_id)
VALUES ('admin', 'admin', 1, 1);

INSERT INTO admin_schema.t_role (create_user, update_user, role_code, role_name)
VALUES ('admin', 'admin', 'admin', '超级管理员');

INSERT INTO admin_schema.t_group_role (create_user, update_user, group_id, role_id)
VALUES ('admin', 'admin', 1, 1);

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id)
VALUES ('admin', 'admin', '用户管理', 0);

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id, menu_url)
VALUES ('admin', 'admin', '用户列表', 1, '/home/user');

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id)
VALUES ('admin', 'admin', '分组管理', 0);

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id, menu_url)
VALUES ('admin', 'admin', '分组列表', 3, '/home/group');

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id)
VALUES ('admin', 'admin', '角色管理', 0);

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id, menu_url)
VALUES ('admin', 'admin', '角色列表', 5, '/home/role');

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id)
VALUES ('admin', 'admin', '菜单管理', 0);

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id, menu_url)
VALUES ('admin', 'admin', '菜单列表', 7, '/home/menu');

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id)
VALUES ('admin', 'admin', '字典管理', 0);

INSERT INTO admin_schema.t_menu (create_user, update_user, menu_name, parent_id, menu_url)
VALUES ('admin', 'admin', '字典列表', 9, '/home/dictionary');

INSERT INTO admin_schema.t_role_menu (create_user, update_user, role_id, menu_id)
VALUES ('admin', 'admin', 1, 1);

INSERT INTO admin_schema.t_role_menu (create_user, update_user, role_id, menu_id)
VALUES ('admin', 'admin', 1, 2);

INSERT INTO admin_schema.t_role_menu (create_user, update_user, role_id, menu_id)
VALUES ('admin', 'admin', 1, 3);

INSERT INTO admin_schema.t_role_menu (create_user, update_user, role_id, menu_id)
VALUES ('admin', 'admin', 1, 4);

INSERT INTO admin_schema.t_role_menu (create_user, update_user, role_id, menu_id)
VALUES ('admin', 'admin', 1, 5);

INSERT INTO admin_schema.t_role_menu (create_user, update_user, role_id, menu_id)
VALUES ('admin', 'admin', 1, 6);

INSERT INTO admin_schema.t_role_menu (create_user, update_user, role_id, menu_id)
VALUES ('admin', 'admin', 1, 7);

INSERT INTO admin_schema.t_role_menu (create_user, update_user, role_id, menu_id)
VALUES ('admin', 'admin', 1, 8);

INSERT INTO admin_schema.t_role_menu (create_user, update_user, role_id, menu_id)
VALUES ('admin', 'admin', 1, 9);

INSERT INTO admin_schema.t_dictionary (create_user, update_user, dictionary_group, dictionary_code, dictionary_name,
                                       show_flag)
VALUES ('admin', 'admin', 'sexType', '0', '男', 0);

INSERT INTO admin_schema.t_dictionary (create_user, update_user, dictionary_group, dictionary_code, dictionary_name,
                                       show_flag)
VALUES ('admin', 'admin', 'sexType', '1', '女', 0);

INSERT INTO admin_schema.t_dictionary (create_user, update_user, dictionary_group, dictionary_code, dictionary_name,
                                       show_flag)
VALUES ('admin', 'admin', 'sexType', '2', '未知', 0);

INSERT INTO admin_schema.t_dictionary (create_user, update_user, dictionary_group, dictionary_code, dictionary_name,
                                       show_flag)
VALUES ('admin', 'admin', 'deleteFlag', '0', '未删除', 0);

INSERT INTO admin_schema.t_dictionary (create_user, update_user, dictionary_group, dictionary_code, dictionary_name,
                                       show_flag)
VALUES ('admin', 'admin', 'deleteFlag', '1', '已删除', 0);

INSERT INTO admin_schema.t_dictionary (create_user, update_user, dictionary_group, dictionary_code, dictionary_name,
                                       show_flag)
VALUES ('admin', 'admin', 'showFlag', '0', '全部', 0);

INSERT INTO admin_schema.t_dictionary (create_user, update_user, dictionary_group, dictionary_code, dictionary_name,
                                       show_flag)
VALUES ('admin', 'admin', 'showFlag', '1', '用户端', 0);

INSERT INTO admin_schema.t_dictionary (create_user, update_user, dictionary_group, dictionary_code, dictionary_name,
                                       show_flag)
VALUES ('admin', 'admin', 'showFlag', '2', '管理端', 0);


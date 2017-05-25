INSERT INTO sys_user (
  username
  , password
  , fullname
  , gender
  , auth_type
  , email
  , phone_number
  , status
  , last_login
  , remark
  , created_at
  , updated_at
) VALUES (
  'admin',
  '2619b778b832467a825cf0406ca3c9c6',
  'Administrator',
  1,
  1,
  '',
  '',
  1,
  NULL,
  '',
  NOW(),
  NOW()
);

INSERT INTO sys_role (
  role_name,
  role_config,
  created_at,
  updated_at
) VALUES
  ('ROLE_ADMIN', '', NOW(), NOW()),
  ('ROLE_USER', '', NOW(), NOW());

INSERT INTO sys_user_role
(user_id,
 role_id,
 created_at,
 updated_at)
VALUES
  (1, 1, NOW(), NOW());

INSERT INTO sys_api (
  api_name, api_url, api_method, api_config, created_at, updated_at
) VALUES
  ('Refresh Authentication Token', '/sysuser/sysuser/refreshToken/{rememberMe}', 2, NULL, now(),
   now());


DELETE FROM sys_role_api
WHERE role_id = 1;

INSERT INTO sys_role_api (
  api_id,
  role_id,
  created_at,
  updated_at
)
  SELECT
    id,
    1,
    NOW(),
    NOW()
  FROM sys_api;

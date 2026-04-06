<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NIPC School API</title>
    <style>
        :root { --blue: #1565c0; --bg: #f5f9ff; }
        body { font-family: system-ui, sans-serif; background: var(--bg); color: #0d2137; margin: 0; padding: 2rem; line-height: 1.5; }
        h1 { color: var(--blue); }
        code, pre { background: #e3eef8; padding: 0.2em 0.4em; border-radius: 4px; }
        ul { max-width: 42rem; }
        a { color: var(--blue); }
        .box { background: #fff; border-radius: 12px; padding: 1.25rem; max-width: 48rem; box-shadow: 0 2px 12px rgba(21,101,192,.12); }
    </style>
</head>
<body>
    <div class="box">
        <h1>NIPC School Management API</h1>
        <p>Running on <strong>Apache port 81</strong>. Copy this folder to <code>C:\xampp\htdocs\school_api</code> (or map the same path).</p>
        <p>MySQL: <strong>127.0.0.1:3307</strong>, database <code>nipc_school</code> — import <code>database/nipc_school.sql</code> via phpMyAdmin.</p>
        <h2>Endpoints (JSON)</h2>
        <ul>
            <li><code>POST api/login.php</code> — body: <code>username</code>, <code>password</code>, <code>role</code> (<code>admin</code> / <code>user</code>)</li>
            <li><code>POST api/logout.php</code></li>
            <li><code>POST api/register.php</code> — register new user</li>
            <li><code>GET|POST api/students.php</code> — list / create (session required)</li>
            <li><code>GET|POST api/scores.php</code> — <code>?student_id=</code> / create (session required)</li>
            <li><code>GET api/school_info.php</code></li>
        </ul>
        <p>Test login (admin): <code>admin</code> / <code>admin123</code>, role <code>admin</code>.</p>
    </div>
</body>
</html>

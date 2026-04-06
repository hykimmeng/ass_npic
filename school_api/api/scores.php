<?php
declare(strict_types=1);

require_once __DIR__ . '/../includes/bootstrap.php';
require_login();

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $sid = trim((string) ($_GET['student_id'] ?? ''));
    if ($sid === '') {
        json_response(400, ['status' => 'error', 'message' => 'student_id query parameter required']);
    }
    $stmt = $pdo->prepare(
        'SELECT id, student_id, subject, score, semester, year FROM scores WHERE student_id = ? ORDER BY id DESC'
    );
    $stmt->execute([$sid]);
    echo json_encode($stmt->fetchAll(), JSON_UNESCAPED_UNICODE);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $body = read_json_body();
    $studentId = trim((string) ($body['student_id'] ?? ''));
    $subject = trim((string) ($body['subject'] ?? ''));
    $scoreRaw = $body['score'] ?? null;
    $semester = isset($body['semester']) ? trim((string) $body['semester']) : '';
    $year = isset($body['year']) ? trim((string) $body['year']) : '';

    if ($studentId === '' || $subject === '' || $scoreRaw === null || $scoreRaw === '') {
        json_response(400, ['status' => 'error', 'message' => 'student_id, subject, and score are required']);
    }

    $score = is_numeric($scoreRaw) ? (float) $scoreRaw : null;
    if ($score === null) {
        json_response(400, ['status' => 'error', 'message' => 'Invalid score']);
    }

    $check = $pdo->prepare('SELECT 1 FROM students WHERE student_id = ?');
    $check->execute([$studentId]);
    if (!$check->fetch()) {
        json_response(404, ['status' => 'error', 'message' => 'Student ID not found. Register the student first.']);
    }

    $semVal = $semester === '' ? null : $semester;
    $yearVal = $year === '' ? null : $year;

    $stmt = $pdo->prepare(
        'INSERT INTO scores (student_id, subject, score, semester, year) VALUES (?, ?, ?, ?, ?)'
    );
    $stmt->execute([$studentId, $subject, $score, $semVal, $yearVal]);

    json_response(200, ['status' => 'success', 'message' => 'Score saved', 'data' => (string) $pdo->lastInsertId()]);
}

json_response(405, ['status' => 'error', 'message' => 'Method not allowed']);

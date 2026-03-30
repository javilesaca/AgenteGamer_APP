# SDD Tasks Skill

This skill provides instructions for breaking down changes into implementation tasks.

## Task Breakdown Guidelines

When breaking down a change into tasks:

1. **Atomicity**: Each task should represent one logical change that can be implemented and tested independently
2. **Sequential vs Parallel**: Identify dependencies between tasks - some must be done in sequence, others can be done in parallel
3. **Runnable in Batches**: Tasks should be groupable into batches that can be implemented together
4. **Clear Acceptance Criteria**: Each task must have testable acceptance criteria

## Task Format

Each task should include:
- Task ID (TASK-001, TASK-002, ...)
- Title
- Description
- File(s) to modify/create
- Dependencies (which tasks must complete before)
- Estimated effort (XS, S, M, L)
- Acceptance criteria

## Effort Estimation

- XS: Extra Small (≤ 2 hours)
- S: Small (2-8 hours)
- M: Medium (8-24 hours)
- L: Large (> 24 hours)

## Dependencies

List task IDs that must be completed before this task can begin. If no dependencies, write "None".

## Output Format

Return a JSON object with:
- status: "success" or "error"
- executive_summary: Brief summary of the task breakdown
- tasks: Array of task objects organized by phase
- next_recommended: Recommended next step (usually "sdd-apply")

## Examples

See the templates in the skill directory for examples of task breakdowns.
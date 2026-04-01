# Skill Registry

**Delegator use only.** Any agent that launches sub-agents reads this registry to resolve compact rules, then injects them directly into sub-agent prompts. Sub-agents do NOT read this registry or individual SKILL.md files.

See `_shared/skill-resolver.md` for the full resolution protocol.

## User Skills

| Trigger | Skill | Path |
|---------|-------|------|
| Angular 20+ architecture with Scope Rule, Screaming Architecture, standalone components, and signals. Trigger: When writing Angular components, services, templates, or making architectural decisions about component placement. | scope-rule-architect-angular | /home/javier/.config/opencode/skills/angular/SKILL.md |
| .NET 9 / ASP.NET Core patterns with Minimal APIs, Clean Architecture, and EF Core. Trigger: When writing C# code, .NET APIs, or Entity Framework models. | dotnet | /home/javier/.config/opencode/skills/dotnet/SKILL.md |
| Django REST Framework patterns. Trigger: When building REST APIs with Django - ViewSets, Serializers, Filters. | django-drf | /home/javier/.config/opencode/skills/django-drf/SKILL.md |
| Go testing patterns for Gentleman.Dots, including Bubbletea TUI testing. Trigger: When writing Go tests, using teatest, or adding test coverage. | go-testing | /home/javier/.config/opencode/skills/go-testing/SKILL.md |
| Next.js 15 App Router patterns. Trigger: When working with Next.js - routing, Server Actions, data fetching. | nextjs-15 | /home/javier/.config/opencode/skills/nextjs-15/SKILL.md |
| Playwright E2E testing patterns. Trigger: When writing E2E tests - Page Objects, selectors, MCP workflow. | playwright | /home/javier/.config/opencode/skills/playwright/SKILL.md |
| Pytest testing patterns for Python. Trigger: When writing Python tests - fixtures, mocking, markers. | pytest | /home/javier/.config/opencode/skills/pytest/SKILL.md |
| React 19 patterns with React Compiler. Trigger: When writing React components - no useMemo/useCallback needed. | react-19 | /home/javier/.config/opencode/skills/react-19/SKILL.md |
| Vercel AI SDK 5 patterns. Trigger: When building AI chat features - breaking changes from v4. | ai-sdk-5 | /home/javier/.config/opencode/skills/ai-sdk-5/SKILL.md |
| Tailwind CSS 4 patterns and best practices. Trigger: When styling with Tailwind - cn(), theme variables, no var() in className. | tailwind-4 | /home/javier/.config/opencode/skills/tailwind-4/SKILL.md |
| TypeScript strict patterns and best practices. Trigger: When writing TypeScript code - types, interfaces, generics. | typescript | /home/javier/.config/opencode/skills/typescript/SKILL.md |
| Zod 4 schema validation patterns. Trigger: When using Zod for validation - breaking changes from v3. | zod-4 | /home/javier/.config/opencode/skills/zod-4/SKILL.md |
| Zustand 5 state management patterns. Trigger: When managing React state with Zustand. | zustand-5 | /home/javier/.config/opencode/skills/zustand-5/SKILL.md |
| Technical review patterns for exercises and candidate submissions. Trigger: When reviewing technical exercises, code assessments, candidate submissions, or take-home tests. | technical-review | /home/javier/.config/opencode/skills/technical-review/SKILL.md |
| Issue creation workflow for Agent Teams Lite following the issue-first enforcement system. Trigger: When creating a GitHub issue, reporting a bug, or requesting a feature. | issue-creation | /home/javier/.config/opencode/skills/issue-creation/SKILL.md |
| PR creation workflow for Agent Teams Lite following the issue-first enforcement system. Trigger: When creating a pull request, opening a PR, or preparing changes for review. | branch-pr | /home/javier/.config/opencode/skills/branch-pr/SKILL.md |
| Parallel adversarial review protocol that launches two independent blind judge sub-agents simultaneously to review the same target, synthesizes their findings, applies fixes, and re-judges until both pass or escalates after 2 iterations. Trigger: When user says "judgment day", "judgment-day", "review adversarial", "dual review", "doble review", "juzgar", "que lo juzguen". | judgment-day | /home/javier/.config/opencode/skills/judgment-day/SKILL.md |
| Review GitHub PRs and Issues with structured analysis for open source projects. Trigger: When user wants to review PRs (even if first asking what's open), analyze issues, or audit PR/issue backlog. Key phrases: "pr review", "revisar pr", "qué PRs hay", "PRs pendientes", "issues abiertos", "sin atención", "hacer review". | pr-review | /home/javier/.config/opencode/skills/pr-review/SKILL.md |
| Creates Jira tasks following Prowler's standard format. Trigger: When user asks to create a Jira task, ticket, or issue. | jira-task | /home/javier/.config/opencode/skills/jira-task/SKILL.md |
| Creates Jira epics for large features following Prowler's standard format. Trigger: When user asks to create an epic, large feature, or multi-task initiative. | jira-epic | /home/javier/.config/opencode/skills/jira-epic/SKILL.md |
| Release workflow for Gentleman-Programming homebrew-tap projects (GGA, Gentleman.Dots). Trigger: When user asks to release, bump version, update homebrew, or publish a new version. | homebrew-release | /home/javier/.config/opencode/skills/homebrew-release/SKILL.md |
| Create slide-deck presentation webs for streams and courses using Gentleman Kanagawa Blur theme with inline SVG diagrams. Trigger: When building a presentation, slide deck, course material, stream web, or talk slides. | stream-deck | /home/javier/.config/opencode/skills/stream-deck/SKILL.md |
| Creates new AI agent skills following the Agent Skills spec. Trigger: When user asks to create a new skill, add agent instructions, or document patterns for AI. | skill-creator | /home/javier/.config/opencode/skills/skill-creator/SKILL.md |

## Compact Rules

Pre-digested rules per skill. Delegators copy matching blocks into sub-agent prompts as `## Project Standards (auto-resolved)`.

### scope-rule-architect-angular
- ALL components MUST be standalone, use input()/output() functions, OnPush change detection, inject() for DI
- Use signals (signal(), computed(), effect()) for state management, avoid ngOnInit
- Use native control flow (@if, @for, @switch), @defer for lazy loading
- Scope Rule: Code used by 2+ features → global/shared; 1 feature → local. NO EXCEPTIONS.
- Screaming Architecture: Feature names describe business functionality, not technical implementation
- No .component/.service/.module suffixes; name should tell behavior
- Use typed reactive forms, NgOptimizedImage for static images
- Path aliases: @features/*, @shared/*, @core/* for clean imports

### dotnet
- Use Minimal APIs for simple endpoints, Controllers for complex ones
- EF Core with Code First migrations, repository pattern optional
- Dependency Injection: constructor injection, services registered in Program.cs
- Clean Architecture: Domain/Application/Infrastructure/Presentation layers
- Result pattern for error handling, avoid exceptions for business logic
- MediatR for CQRS, FluentValidation for validation
- JWT authentication with Refresh Tokens, role-based authorization
- Global exception handling middleware, structured logging with Serilog

### django-drf
- Use ViewSets for CRUD operations, APIViews for custom logic
- Serializers for data validation and transformation
- ModelViewSet + Router for automatic URL configuration
- Pagination, filtering, ordering built-in
- Authentication: Token, Session, JWT (djangorestframework-simplejwt)
- Permissions: IsAuthenticated, IsAdminUser, custom permissions
- Throttling for rate limiting, caching with Django cache framework
- Django signals for cross-cutting concerns, Celery for async tasks

### go-testing
- Table-driven tests with t.Run() for subtests
- Use testify/assert for assertions, testify/mock for mocking
- Benchmarks with testing.B, fuzz testing with testing.F
- Test helpers with t.Helper(), subtests for organization
- Mock external dependencies, test interfaces not implementations
- Coverage reports with -cover, race detection with -race
- Bubbletea TUI testing with teatest package
- Integration tests with test containers, end-to-end with真实环境

### nextjs-15
- App Router with layouts, loading, error boundaries
- Server Components by default, 'use client' only for interactivity
- Server Actions for mutations, form handling with useFormState
- Parallel routes (@folder) for modals, interception for overlays
- Caching: fetch with { next: { revalidate: N } }, unstable_noStore for dynamic
- Metadata API for SEO, generateMetadata for dynamic titles
- Route Handlers for API, Middleware for auth/redirects
- Partial Prerendering (PPR) for hybrid static/dynamic

### playwright
- Page Object Model for maintainability
- Locator API over selectors, avoid page.locator('*')
- Auto-waiting for elements, actionability checks
- Test fixtures for setup/teardown
- Parallel test execution, sharding for CI
- Trace viewer for debugging, video recording
- API testing with request context
- Visual comparison with toHaveScreenshot()

### pytest
- Fixtures for setup/teardown, conftest.py for shared fixtures
- Parametrize for data-driven tests
- Markers for test categorization (@pytest.mark.slow)
- Mocking with unittest.mock, pytest-mock plugin
- Assertions with plain assert, no need for assertEqual
- Plugins: pytest-cov, pytest-xdist, pytest-django
- Fixtures can be nested, yield for cleanup
- Custom plugins for project-specific patterns

### react-19
- No useMemo/useCallback — React Compiler handles memoization automatically
- use() hook for promises/context, replaces useEffect for data fetching
- Server Components by default, add 'use client' only for interactivity/hooks
- ref is a regular prop — no forwardRef needed
- Actions: use useActionState for form mutations, useOptimistic for optimistic UI
- Metadata: export metadata object from page/layout, no <Head> component
- useFormStatus for form state, useTransition for non-blocking updates
- React Compiler: automatic optimization, no manual memoization needed

### ai-sdk-5
- Stream text with streamText(), generate with generateText()
- Use AI SDK React hooks: useChat, useCompletion, useAssistant
- Multi-modal: text, image, audio inputs/outputs
- Tool calling with streaming, parallel tool execution
- RAG patterns with embeddings and vector stores
- Provider abstraction: OpenAI, Anthropic, Google, etc.
- Error handling: AI SDK errors, retry mechanisms
- Edge runtime compatible, streaming responses

### tailwind-4
- CSS variables for theme, no var() in className
- cn() utility for conditional classes (clsx + tailwind-merge)
- Container queries, @apply for complex patterns
- Dark mode with media or class strategy
- JIT compiler, no purge needed
- Arbitrary values: [length:10px], [color:#000]
- Animation with @keyframes, transition utilities
- Responsive design with sm:, md:, lg: prefixes

### typescript
- Strict mode: strict: true in tsconfig
- No any, use unknown for unknown values
- Discriminated unions for type safety
- Utility types: Partial, Required, Pick, Omit, Record
- Generics for reusable components/functions
- Type guards: typeof, instanceof, custom predicates
- Module augmentation for third-party libraries
- Declaration files (.d.ts) for type definitions

### zod-4
- Schema-first validation, infer types from schemas
- Chainable API: z.string().email().min(5)
- Refinements for custom validation
- coerce for input transformation
- Discriminated unions with z.discriminatedUnion()
- Branded types for nominal typing
- pipe() for composable transformations
- Error customization with errorMap

### zustand-5
- Store creation with create() hook
- Selectors for performance, avoid unnecessary re-renders
- Middleware: devtools, persist, immer, subscribeWithSelector
- Shallow equality for object comparisons
- Async actions, no need for useEffect for store updates
- TypeScript: typed store, typed hooks
- Server-side rendering compatibility
- combine() for multiple store slices

### technical-review
- Rubric-based evaluation: correctness, efficiency, readability, testing
- Code should be production-ready, not just "works"
- Check for SOLID principles, DRY, KISS
- Security: input validation, SQL injection, XSS
- Performance: algorithms, data structures, memory usage
- Testing: coverage, edge cases, test quality
- Documentation: comments, README, API docs
- Constructive feedback, specific suggestions

### issue-creation
- Issue-first: always create issue before PR
- Template: problem, solution, acceptance criteria
- Labels: bug, feature, enhancement, documentation
- Assignees and milestones for tracking
- Reference in commits: Closes #123
- Description with steps to reproduce for bugs
- Mockups/screenshots for UI changes
- Related issues linked

### branch-pr
- Branch naming: feature/, bugfix/, hotfix/, release/
- PR template: what, why, how, testing
- Small, focused PRs (<400 lines)
- Reviewers assigned, CI passing
- Squash merge for clean history
- Conventional commits in PR title
- Description with context and screenshots
- Link to issue, update issue after merge

### judgment-day
- Parallel blind review with two independent judges
- Synthesize findings: confirmed, suspect, contradictions
- Fix confirmed issues, re-judge after fixes
- Maximum 2 iterations before escalating
- Skill resolution: inject compact rules into judge prompts
- Output: verdict table with severity, status
- Blocking rules: no approval until round 2 clean
- Escalation format when issues remain after iterations

### pr-review
- Structured analysis: correctness, performance, security, maintainability
- Line-specific comments with suggestions
- Check for: error handling, edge cases, naming, documentation
- Approve, request changes, or comment
- Respect project conventions and style
- Constructive feedback, explain WHY
- Priority: critical > major > minor > nit
- Summary of changes, impact assessment

### jira-task
- Title: clear, concise, action-oriented
- Description: problem, solution, acceptance criteria
- Story points: Fibonacci (1,2,3,5,8,13)
- Assignee, reporter, labels, components
- Linked issues: blocks, blocked by, relates to
- Subtasks for complex implementation
- Sprint planning, estimation
- Definition of done checklist

### jira-epic
- Large feature spanning multiple sprints
- Break down into stories/tasks
- Business value and objectives
- Dependencies and blockers
- Timeline and milestones
- Stakeholder communication
- Progress tracking with burndown
- Success metrics

### homebrew-release
- Version bump in formula
- Update SHA256 checksum
- Test installation: brew install --build-from-source
- Create PR to homebrew-tap repository
- Release notes with changes
- Semantic versioning (MAJOR.MINOR.PATCH)
- Automated with GitHub Actions
- Rollback plan for broken releases

### stream-deck
- Slide deck for streams/courses
- Gentleman Kanagawa Blur theme
- Inline SVG diagrams for architecture
- Responsive design, mobile-friendly
- Interactive elements: code demos, polls
- Accessibility: alt text, keyboard navigation
- Export to PDF/HTML
- Version control with Git

### skill-creator
- Follow Agent Skills spec format
- Purpose, when-to-use, what-to-do, rules
- Include examples and patterns
- Test with real scenarios
- Version and metadata
- Documentation and examples
- Backward compatibility
- Community guidelines

## Project Conventions

| File | Path | Notes |
|------|------|-------|
| No project convention files found | - | No agents.md, AGENTS.md, .cursorrules, GEMINI.md, or copilot-instructions.md found in project root |

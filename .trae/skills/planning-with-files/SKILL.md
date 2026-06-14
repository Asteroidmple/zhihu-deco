---
name: "planning-with-files"
description: "Transforms workflow to use persistent markdown files for planning, progress tracking, and knowledge storage. Invoke when starting complex tasks, need progress tracking, or want to maintain context across sessions."
---

# Planning with Files

A skill that transforms your workflow to use persistent markdown files for planning, progress tracking, and knowledge storage — the exact pattern that made Manus worth billions.

## Overview

This skill implements the Manus-style persistent planning workflow, using markdown files to maintain context, track progress, and store knowledge across sessions.

## Commands

### Main Commands

| Command | Autocomplete | Description |
|---------|-------------|-------------|
| `/planning-with-files:plan` | Type `/plan` | Start planning session (v2.11.0+) |
| `/planning-with-files:status` | Type `/plan:status` | Show planning progress at a glance (v2.15.0+) |
| `/planning-with-files:start` | Type `/planning` | Original start command |

### Alternative Usage

If you want `/planning-with-files` (without prefix), copy skills to your local folder:

**macOS/Linux:**
```bash
cp -r ~/.claude/plugins/cache/planning-with-files/planning-with-files/*/skills/planning-with-files ~/.claude/skills/
```

**Windows (PowerShell):**
```powershell
Copy-Item -Recurse -Force "$env:USERPROFILE\.claude\plugins\cache\planning-with-files\planning-with-files\*\skills\planning-with-files" "$env:USERPROFILE\.claude\skills\"
```

## Features

### Session Recovery
When your context fills up and you run `/clear`, this skill automatically recovers your previous session.

**How it works:**
1. Checks for previous session data in `~/.claude/projects/`
2. Finds when planning files were last updated
3. Extracts conversation that happened after (potentially lost context)
4. Shows a catchup report so you can sync

**Pro tip:** Disable auto-compact to maximize context before clearing:
```json
{ "autoCompact": false }
```

### Supported IDEs (16 Platforms)

| IDE | Status | Format |
|-----|--------|--------|
| Claude Code | ✅ Full Support | Plugin + SKILL.md |
| Gemini CLI | ✅ Full Support | Agent Skills |
| OpenClaw | ✅ Full Support | Workspace/Local Skills |
| Kiro | ✅ Full Support | Steering Files |
| Cursor | ✅ Full Support | Skills + Hooks |
| Continue | ✅ Full Support | Skills + Prompt files |
| Kilocode | ✅ Full Support | Skills |
| OpenCode | ⚠️ Partial Support | Personal/Project Skill |
| Codex | ✅ Full Support | Personal Skill |
| FactoryAI Droid | ✅ Full Support | Workspace/Personal Skill |
| Antigravity | ✅ Full Support | Workspace/Personal Skill |
| CodeBuddy | ✅ Full Support | Workspace/Personal Skill |
| AdaL CLI (Sylph AI) | ✅ Full Support | Personal/Project Skills |
| Pi Agent | ✅ Full Support | Agent Skills |
| GitHub Copilot | ✅ Full Support | Hooks |
| Mastra Code | ✅ Full Support | Skills + Hooks |

## Installation

### Quick Install
```bash
npx skills add OthmanAdi/planning-with-files --skill planning-with-files -g
```

Works with Claude Code, Cursor, Codex, Gemini CLI, and 40+ agents supporting the Agent Skills spec.

### Claude Code Plugin (Advanced Features)
For Claude Code-specific features like `/plan` autocomplete commands:

```bash
/plugin marketplace add OthmanAdi/planning-with-files
/plugin install planning-with-files@planning-with-files
```

## Workflow Pattern

1. **Start Planning**: Use `/planning-with-files:plan` to create a planning file
2. **Track Progress**: The skill maintains context in markdown files
3. **Session Recovery**: Automatically recovers context after `/clear`
4. **Knowledge Storage**: All planning and decisions are persisted

## Best Practices

- Use planning files for complex, multi-step tasks
- Review the catchup report after session recovery
- Keep planning files organized by project/task
- Use `/plan:status` to quickly check progress

## Community Extensions

The community has built several extensions:

| Extension | Author | Features |
|-----------|--------|----------|
| devis | @st01cs | Interview-first workflow, /devis:intv and /devis:impl commands |
| multi-manus-planning | @kmichels | Multi-project support, SessionStart git sync |
| plan-cascade | @Taoidle | Multi-level task orchestration, parallel execution |
| agentfund-skill | @RioTheGreat-ai | Crowdfunding for AI agents with milestone-based escrow |

## Resources

- **GitHub**: https://github.com/OthmanAdi/planning-with-files
- **Current Version**: v2.18.2
- **CHANGELOG**: View all releases and updates
- **Contributors**: See full list in CONTRIBUTORS.md

## Experimental Features

**Isolated Parallel Planning** (`.planning/{uuid}/` folders) is being tested on `experimental/isolated-planning` branch. Try it and share feedback!

## Note

This skill implements the workflow pattern behind the $2B Manus acquisition. It transforms your AI assistant workflow to use persistent files for planning and knowledge management.

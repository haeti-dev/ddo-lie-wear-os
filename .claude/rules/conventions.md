# Conventions

## Commits

- Format: `type : summary` — the summary is written in Korean; type ∈ feat / fix / refactor / design / chore / docs
- One concern per commit; never mix file moves with code changes

## Branches

- `<type>/<kebab-topic>` — e.g. `refactor/monorepo`, `feature/watchos-scaffold`

## PRs

- Create with the `/pr` skill
- Show the final title, body, and commit list to the user and get explicit approval **before** running `gh pr create`
- Body structure: background → changes → troubleshooting (if any) → verification (build output, screenshots)
- Keep PRs reviewable; split structural moves from functional changes

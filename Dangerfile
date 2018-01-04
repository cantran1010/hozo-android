# Dismiss warnings out of diff range
dismiss_out_of_range_messages({
  error: false,
  warning: true,
  message: true,
  markdown: true
})

# Android Lint
android_lint.filtering = true
android_lint.report_file = "app/build/reports/lint-results.xml"
android_lint.lint(inline_mode: true)

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("This Pull Request is too big, which may cause trouble if we want to revert it. Please consider splitting it into multiple ones.") if git.lines_of_code > 500

# To ensure a clean commits history, do not merge master back to your working
# branch
if git.commits.any? { |c| c.message =~ /^Merge branch/ }
  fail("Please rebase to get rid of the merge commits in this Pull Request")
end

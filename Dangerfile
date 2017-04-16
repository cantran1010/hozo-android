# Android Lint
android_lint.lint

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("This Pull Request is too big, which may cause trouble if we want to revert it. Please consider splitting it into multiple Pull Requests.") if git.lines_of_code > 500

# To ensure a clean commits history, do not merge master back to your working
# branch
if git.commits.any? { |c| c.message =~ /^Merge branch/ }
  fail("Please rebase to get rid of the merge commits in this Pull Request")
end

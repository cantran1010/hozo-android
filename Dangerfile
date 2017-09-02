lgtm_num = Random.new.rand(1..30).to_s.rjust(2, '0')
lgtm.check_lgtm image_url: "https://raw.githubusercontent.com/rela1470/lgtm/master/momoka/momoka_#{lgtm_num}.jpg"

# Android Lint
android_lint.report_file = "app/build/reports/lint-results.xml"
android_lint.lint

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("This Pull Request is too big, which may cause trouble if we want to revert it. Please consider splitting it into multiple ones.") if git.lines_of_code > 500

# To ensure a clean commits history, do not merge master back to your working
# branch
if git.commits.any? { |c| c.message =~ /^Merge branch/ }
  fail("Please rebase to get rid of the merge commits in this Pull Request")
end

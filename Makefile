.PHONY: localize
localize: vendor ## Generate localization files
	@echo "==> Generating localization files"
	@bundle exec babelish csv2android

.PHONY: vendor
vendor:
	@bundle install

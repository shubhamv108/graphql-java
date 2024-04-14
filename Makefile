SHELL := /bin/bash
OS := $(shell uname)

define start-services
	@docker compose -f compose.yaml up --force-recreate -d --remove-orphans db
endef

define teardown
	@docker compose -f compose.yaml rm -f -v -s
	@docker system prune -f --volumes
endef

define local-setup
	$(call start-services)
endef

teardown:
	$(call teardown)

local-setup: teardown
	$(call local-setup)
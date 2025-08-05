# Compatibility

This is an outline of the project's approach to compatibility.

## Versioning scheme

`MAJOR.MINOR.PATCH` - following semantic versioning.

## Deprecation cycles

* `MAJOR.MINOR.0` - introduce new never before seen API.
* `MAJOR.MINOR + >=1` - should a change be required deprecate with `WARNING`.
* `MAJOR.MINOR + >=2` - deprecate with `ERROR`.
* `MAJOR.MINOR + >=3` - deprecate with `HIDDEN`.
* `MAJOR + >=1` - consider removing entirely the `HIDDEN` API.

The above should ensure full backward binary compatibility within `MAJOR` versions.

## Pre-Releases

From time to time we reserve the right to introduce fast moving breaking changes as either
pre-release versions or under experimental annoations. These will be clearly documented in both code
and the `CHANGELOG.md`.
# webprotege-initial-revision-history-service

This is a WebProtégé microservice that is responsible for generating an intial revision history from an existing ontology.  It pulls a set of ontology documents from a (MinIO) bucket and generates the revision history to create these ontologies from scratch.  It then places the revision history into a new bucket.

## Status

[![Java Continous Integration](https://github.com/protegeproject/webprotege-initial-revision-history-service/actions/workflows/ci.yaml/badge.svg)](https://github.com/protegeproject/webprotege-initial-revision-history-service/actions/workflows/ci.yaml)

[![Publish to Docker Hub](https://github.com/protegeproject/webprotege-initial-revision-history-service/actions/workflows/pub-docker-hub.yaml/badge.svg)](https://github.com/protegeproject/webprotege-initial-revision-history-service/actions/workflows/pub-docker-hub.yaml)

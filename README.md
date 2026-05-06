# 🚀 CloudEagle DevOps Assignment – sync-service

## 📌 Overview

This project demonstrates the design and implementation of a scalable, secure, and cost-effective CI/CD pipeline and infrastructure for a Spring Boot backend service (`sync-service`) deployed on Google Cloud Platform.

The service connects to MongoDB and is deployed across three environments:

* QA
* Staging
* Production

---

# 🏗️ Architecture Overview

## 🔹 High-Level Flow

```
User → Cloud Run → MongoDB Atlas

CI/CD:
GitHub → Jenkins → Artifact Registry → Cloud Run
```

## 🔹 Components

* **Compute:** Cloud Run (serverless container platform)
* **Database:** MongoDB Atlas (managed NoSQL DB)
* **CI/CD:** Jenkins
* **Container Registry:** Google Artifact Registry
* **Secrets:** GCP Secret Manager

---

# 🌿 Branching Strategy

## 🔹 Branch Structure

```
main        → Production
staging     → Staging
qa          → QA
feature/*   → Development
```

## 🔹 Workflow

```
feature/* → qa → staging → main
```

## 🔹 Branch to Environment Mapping

| Branch  | Environment |
| ------- | ----------- |
| qa      | QA          |
| staging | Staging     |
| main    | Production  |

## 🔹 Safety Controls

* `main` branch is protected
* PR approval required
* CI checks must pass before merge
* Manual approval required for production deployment

---

# ⚙️ CI/CD Pipeline (Jenkins)

## 🔹 Pipeline Stages

1. Checkout Code
2. Build Application (Maven)
3. Run Unit Tests
4. Build Docker Image
5. Push Image to Artifact Registry
6. Deploy to Cloud Run

---

## 🔹 PR vs Merge Behavior

### On Pull Request:

* Build & Test
* ❌ No Deployment

### On Merge:

* Build Docker Image
* Push to Registry
* Deploy based on branch

---

## 🔹 Deployment Logic

| Branch  | Deployment Type          |
| ------- | ------------------------ |
| qa      | Auto Deploy              |
| staging | Auto Deploy              |
| main    | Manual Approval + Deploy |

---

# 🚀 Deployment Strategy

## 🔹 Strategy Used: Rolling Deployment

### Why Rolling?

* Zero downtime
* Cost efficient
* Simple to implement

### Why Not Blue/Green?

* Requires duplicate infrastructure
* Higher cost

### Why Not Recreate?

* Causes downtime

---

## 🔹 Zero Downtime Approach

* Health checks enabled
* Gradual instance scaling via Cloud Run
* Traffic managed automatically

---

## 🔹 Configuration

Configuration is managed using environment variables instead of environment-specific configuration files.

This approach ensures:
- Separation of config from code
- Better security practices
- Flexibility across environments

---

## 🔐 Secrets Handling

Sensitive data such as MongoDB connection strings and API keys are stored securely in Google Cloud Secret Manager.

Secrets are accessed by:
- Cloud Run via environment variables
- Jenkins via credentials binding

This ensures that no sensitive data is stored in the codebase.

* Injected via environment variables

---

## 🔹 Secrets Handling

* Stored in GCP Secret Manager

* Includes:

  * MongoDB URI

* Accessed securely via:

  * Cloud Run environment variables
  * Jenkins credentials binding

---

# 🔁 Rollback Strategy

* Each deployment uses a versioned Docker image
* Example:

  ```
  build-101, build-102
  ```

## 🔹 Rollback Method

* Redeploy previous stable image
* Update Cloud Run traffic to previous revision

---

# ☁️ Infrastructure Design

## 🔹 Compute Choice: Cloud Run

### Justification:

* Fully managed (no server management)
* Auto-scaling (including scale-to-zero)
* Cost-efficient for startup workloads

---

## 🔹 Database: MongoDB Atlas

### Justification:

* Managed high availability
* Minimal operational overhead

---

## 🔹 Networking

* Controlled access via IAM roles
* Optional VPC connector for private access

---

## 🔹 IAM & Security

* Service accounts with least privilege
* Secure access to secrets via IAM roles

---

# 📊 Monitoring & Logging

* Cloud Logging for application logs

* Cloud Monitoring for:

  * CPU usage
  * Error rates
  * Latency

* Alerts can be configured for production issues

---

# 💰 Cost Optimization

* Cloud Run scale-to-zero reduces idle cost
* Managed services reduce operational overhead
* Avoided over-provisioning (no GKE/VM clusters)

---

# 📁 Repository Structure

```
.
├── README.md
├── Jenkinsfile
├── Dockerfile
├── architecture.png
└── deploy.sh
```

---

# ✅ Conclusion

This solution provides:

* Scalable deployment using serverless architecture
* Secure and centralized secret management
* Automated CI/CD pipeline with safe production controls
* Cost-efficient infrastructure suitable for startups

---

# 👤 Author

Sreejith
DevOps Engineer

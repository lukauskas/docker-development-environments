FROM python:3.6

# Some ideas from https://github.com/in-silico/judge-bot/blob/768238c1eb0875a1d7800343755d70fd432a28c2/debian/Dockerfile
# Install R, libhdf5 and nodejs
RUN echo "deb http://ftp.de.debian.org/debian testing contrib" >> /etc/apt/sources.list \
    && echo "deb http://ftp.de.debian.org/debian testing non-free" >> /etc/apt/sources.list \
    && echo "deb http://ftp.de.debian.org/debian testing main" >> /etc/apt/sources.list \
    && apt-get update \
    && apt-get install -y libhdf5-dev \
                          nodejs \
                          r-base \
    && apt-get -yq autoremove \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install some needed R packages
RUN Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); source("https://bioconductor.org/biocLite.R"); biocLite("minet")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); source("http://bioconductor.org/biocLite.R"); biocLite(c("AnnotationDbi", "impute", "GO.db", "preprocessCore")); install.packages("WGCNA")' \
    && Rscript -e 'chooseCRANmirror(graphics=FALSE, ind=50); install.packages("precrec")'

RUN pip install --upgrade pip \
    && pip install --upgrade click==6.7 \
                             cython==0.26 \
                             fastcluster==1.1.24 \
                             jupyter==1.0.0 \
                             lxml==3.8.0 \
                             matplotlib==2.0.2 \
                             mygene==3.0.0 \
                             numpy==1.13.1 \
                             networkx==2.0 \
                             pandas==0.20.3 \
                             PyYAML==3.12 \
                             requests==2.18.4 \
                             rpy2==2.9.0 \
                             scikits.bootstrap==0.3.3 \
                             scikit-learn==0.19.0 \
                             scipy==0.19.1 \
                             git+https://github.com/mwaskom/seaborn.git@9eaaa535b3648bbfced5ad78f81e1efc0d02167c \
                             statsmodels==0.8.0 \
                             tables==3.4.2 \
                             tqdm==4.15.0 \
                             xlrd==1.0.0 \
                             XlsxWriter==1.0.2 \
    && rm -rf ~/.cache/pip

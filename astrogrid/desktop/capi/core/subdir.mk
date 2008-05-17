################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
./core/acrtypes.c 

CPP_SRCS += \
./core/AR.cpp \
./core/JavaProperties.cpp \
./core/URL.cpp \
./core/arcontainers.cpp \
./core/plastic.cpp 

OBJS += \
./core/AR.o \
./core/JavaProperties.o \
./core/URL.o \
./core/acrtypes.o \
./core/arcontainers.o \
./core/plastic.o 

C_DEPS += \
./core/acrtypes.d 

CPP_DEPS += \
./core/AR.d \
./core/JavaProperties.d \
./core/URL.d \
./core/arcontainers.d \
./core/plastic.d 


# Each subdirectory must supply rules for building sources it contributes
core/%.o: ./core/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I"./capi/core" -I"./capi/xmlrpc" -I"./capi" -I/Applications/itt/idl64/external/include -O0 -g3 -Wall -c -fmessage-length=0 -dynamic -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

core/%.o: ./core/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -I"./capi/core" -I"./capi" -I/Applications/itt/idl64/external/include -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


